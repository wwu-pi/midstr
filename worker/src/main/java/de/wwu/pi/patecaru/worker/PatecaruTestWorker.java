package de.wwu.pi.patecaru.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

import de.wwu.pi.patecaru.RunTimesWorker;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruWorkpackage;
import de.wwu.pi.patecaru.worker.listener.PatecaruWorkerRunListener;

/**
 * The actual worker.
 * @author a_fuch05
 */
public class PatecaruTestWorker {
	
	private String workerId;
	
	public PatecaruTestWorker(String workerId) {
		this.workerId = workerId;
	}
	
	public String getWorkerId() {
		return workerId;
	}

	/**
	 * Work on the given work package.
	 * @param workpackage the work package to work on
	 */
	public void work(PatecaruWorkpackage workpackage, PatecaruWorkerRunListener workerListener) {
		RunTimesWorker.startWorking();
		
		workerListener.startWorkpackageTest();
		
		Map<PatecaruTestReference, Result> testResults = new HashMap<PatecaruTestReference, Result>();
		for (PatecaruTestReference testReference : workpackage.getTestCaseReferences()) {
			RunTimesWorker.startWorkOnTestReference(testReference);
			// create the JUnit description and runner from the given class and method
			Description method = Description.createTestDescription(testReference.getTestClass(), testReference.getTestMethod());
			Request request = Request.aClass(testReference.getTestClass()).filterWith(method);
			Runner runner = request.getRunner();
			
			// create the run notifier
			final RunNotifier notifier = new RunNotifier();
			
			// create the test result and add it as a listener to the run notifier
			Result result = new Result();
			RunListener listener = result.createListener();
			notifier.addListener(listener);
			
			// execute the test run
			try {
				notifier.fireTestRunStarted(runner.getDescription());
				runner.run(notifier);
				notifier.fireTestRunFinished(result);
				testResults.put(testReference, result);
			} catch (StoppedByUserException e) {
				// not interesting, see https://bugs.eclipse.org/329498
			} finally {
				notifier.removeListener(listener);
			}
			RunTimesWorker.endWorkOnTestReference(testReference);
		}
		
		workerListener.finishedWorkpackageTest(testResults);
		RunTimesWorker.endWorking();
	}
	
	
}
