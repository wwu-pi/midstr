package de.wwu.pi.patecaru.eclipse.runner;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.jdt.internal.junit.runner.IStopListener;
import org.eclipse.jdt.internal.junit.runner.ITestReference;
import org.eclipse.jdt.internal.junit.runner.MessageIds;
import org.eclipse.jdt.internal.junit.runner.RemoteTestRunner;
import org.eclipse.jdt.internal.junit.runner.TestExecution;
import org.eclipse.jdt.internal.junit4.runner.JUnit4Identifier;
import org.eclipse.jdt.internal.junit4.runner.JUnit4TestListener;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;
import de.wwu.pi.patecaru.mq.TestBroker;

@SuppressWarnings("restriction")
public class PatecaruStarterEclipseTestRunner extends RemoteTestRunner {

	private TestBroker testBroker;

	public TestBroker getTestBroker() {
		return testBroker;
	}

	public void setTestBroker(TestBroker testBroker) {
		this.testBroker = testBroker;
	}

	/**
	 * Launches the test Runners for the supplied arguments
	 * @param args arguments
	 */
	public static void main(String[] args) {
		try {
			PatecaruStarterEclipseTestRunner testRunServer = new PatecaruStarterEclipseTestRunner();
			testRunServer.setTestBroker(new TestBroker());
			testRunServer.init(args);
			testRunServer.run();
		} catch (Throwable e) {
			e.printStackTrace(); // don't allow System.exit(0) to swallow
									// exceptions
		} finally {
			// fix for 14434
			System.exit(0);
		}
	}

	private void runAllTestsNew(List<PatecaruTestReference> testReferences, RunListener runListener) {
		for (PatecaruTestReference testReference : testReferences) {
			final RunNotifier notifier = new RunNotifier();
			notifier.addListener(runListener);

			Result result = new Result();
			RunListener listener = result.createListener();
			notifier.addListener(listener);

			try {
				notifier.fireTestRunStarted(testReference.getDescription());
				testReference.getRunner().run(notifier);
				notifier.fireTestRunFinished(result);
			} catch (StoppedByUserException e) {
				// not interesting, see https://bugs.eclipse.org/329498
			} finally {
				notifier.removeListener(listener);
			}
		}
	}

	private void runAllTests(String[] testClassNames, RunListener runListener) throws ClassNotFoundException {
		long testStartTime = System.currentTimeMillis();

		for (String testClassName : testClassNames) {
			Class<?> testClass = ClassLoader.getSystemClassLoader().loadClass(testClassName);
			List<PatecaruTestReference> testReferences = createMethodTestReferences(testClass);

			int count = testReferences.size();
			notifyTestRunStarted(count);
			if (count == 0) {
				this.sendMessage(MessageIds.TEST_RUN_END + 0);
				this.flush();
				return;
			}

			for (PatecaruTestReference testReference : testReferences) {

				final RunNotifier notifier = new RunNotifier();
				notifier.addListener(runListener);

				Result result = new Result();
				RunListener listener = result.createListener();
				notifier.addListener(listener);

				try {
					notifier.fireTestRunStarted(testReference.getDescription());
					testReference.getRunner().run(notifier);
					notifier.fireTestRunFinished(result);
				} catch (StoppedByUserException e) {
					// not interesting, see https://bugs.eclipse.org/329498
				} finally {
					notifier.removeListener(listener);
				}
			}
		}
		notifyListenersOfTestEnd(null, testStartTime);
	}

	private PatecaruTestReference createClassTestReference(Class<?> clazz) {
		Request request = Request.classWithoutSuiteMethod(clazz);
		Runner runner = request.getRunner();
		Description description = runner.getDescription();
		return new PatecaruTestReference(runner, description);
	}

	/**
	 * TODO At this point, we should be generating disjunct WorkPackages
	 * @param clazz
	 * @return
	 */
	private List<PatecaruTestReference> createMethodTestReferences(Class<?> clazz) {
		List<PatecaruTestReference> testReferences = new ArrayList<>();
		for (Method m : getAllTestMethods(clazz)) {
			Request request = Request.method(clazz, m.getName());
			Runner runner = request.getRunner();
			Description description = runner.getDescription();
			testReferences.add(new PatecaruTestReference(runner, description));
		}
		return testReferences;
	}

	private List<PatecaruTestReference> createMethodTestReferences(String... clazzes) {
		List<PatecaruTestReference> testReferences = new ArrayList<>();
		for (String clazzName : clazzes) {
			try {
				Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(clazzName);
				for (Method m : getAllTestMethods(clazz)) {
					Request request = Request.method(clazz, m.getName());
					Runner runner = request.getRunner();
					Description description = runner.getDescription();
					testReferences.add(new PatecaruTestReference(runner, description));
				}
			} catch (Exception e) {
				// TODO: error handling
			}
		}
		return testReferences;
	}

	// this is JUnit 4 only... (no JUnit 3 support for now)
	private List<Method> getAllTestMethods(Class<?> clazz) {
		List<Method> testMethods = new ArrayList<>();
		for (Method m : clazz.getMethods()) {
			if (m.getAnnotation(Test.class) != null) {
				testMethods.add(m);
			}
		}
		return testMethods;
	}

	private void sendTree(Description description) {
		if (description.isTest()) {
			this.visitTreeEntry(new JUnit4Identifier(description), false, 1);
		} else {
			this.visitTreeEntry(new JUnit4Identifier(description), true, description.getChildren().size());
			for (Description child : description.getChildren()) {
				sendTree(child);
			}
		}
	}

	@Override
	public void runTests(String[] testClassNames, String testName, TestExecution execution) {
		List<PatecaruTestReference> testReferences = createMethodTestReferences(testClassNames);
		int count = testReferences.size();
		notifyTestRunStarted(count);
		if (count == 0) {
			this.sendMessage(MessageIds.TEST_RUN_END + 0);
			this.flush();
			return;
		}

		for (PatecaruTestReference testReference : testReferences) {
			Description description = testReference.getDescription();
			sendTree(description);
		}

		long testStartTime = System.currentTimeMillis();

		RunListener eclipseRunListener = new JUnit4TestListener(execution.getListener());
		runAllTestsNew(testReferences, eclipseRunListener);
		notifyListenersOfTestEnd(execution, testStartTime);
	}
	
	
}
