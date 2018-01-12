package de.wwu.pi.patecaru.runner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import de.wwu.pi.patecaru.RunTimes;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruResult;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;
import de.wwu.pi.patecaru.env.PatecaruTestEnvironmentManager;
import de.wwu.pi.patecaru.runner.callback.RunnerCallback;
import de.wwu.pi.patecaru.runner.listener.PatecaruRunListenerImpl;

/**
 * Main Runner for Patecaru. Can be used from any tool that supports JUnit Runners.
 * @author vincentvonhof, andreasfuchs
 *
 */
public class PatecaruTestRunner {
	
	private long runTimeInMillis;
	private boolean wait;
	protected PatecaruRunListenerImpl testRunListener;
	protected String[] testClassNames;
	protected int numberOfWorkerThreads = 5;
	protected List<PatecaruResult> workerPackageResultList;
	protected List<PatecaruTestReference> testCaseRerences;
	
	public PatecaruTestRunner() {
		this.workerPackageResultList = new ArrayList<PatecaruResult>();
	}
	
	public void addResult(PatecaruResult result) {
		this.workerPackageResultList.add(result);
	}
	
//	protected boolean isTestRunFinished() {
//		Set<PatecaruTestReference> finishedTestCases = new HashSet<PatecaruTestReference>();
//		for(PatecaruResult result : this.resultList) {
//			for(PatecaruTestReference  tr : result.getTestCases()) {
//				finishedTestCases.add(tr);
//			}
//		}
//		return resultList.equals(finishedTestCases);
//	}

	
	
	public static void main(String[] args) {
		final long start = System.currentTimeMillis();
		RunTimes.startRun();
		
		final PatecaruTestRunner runner = new PatecaruTestRunner();
		runner.init(args);
		if(!PatecaruTestEnvironmentManager.getInstance().isSetup()) {
			PatecaruTestEnvironmentManager.getInstance().setup(runner.numberOfWorkerThreads);
		}
		RunTimes.endSetupEnvironment();
		runner.wait = true;
		runner.registerRunner(new RunnerCallback() {
			public void callback() {
				runner.wait = false;
				RunTimes.endTestRun();
				runner.runTimeInMillis = System.currentTimeMillis() - start;
			}
		});
		RunTimes.endRegisterRunners();
		
		RunTimes.startTestRun();
		runner.runTests();
		
		while(runner.wait) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
				
		System.out.println("Complete run time: " + runner.runTimeInMillis + "ms");
		
//		runner.printXMLResult();
		RunTimes.endRun();
		
		RunTimes.printTimes();
	}

	public void runTests() {
		runTests(this.testClassNames);
	}
	
	public void runTests(String[] testClassNames) {
		this.testCaseRerences = createMethodTestReferences(testClassNames);
		try {
			PatecaruTestEnvironmentManager.getInstance().getTestBroker().runTests(this.testCaseRerences);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<PatecaruTestReference> createMethodTestReferences(String... clazzes) {
		List<PatecaruTestReference> testReferences = new ArrayList<PatecaruTestReference>();
		for (String clazzName : clazzes) {
			try {
				Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(clazzName);
				for (Method method : getAllTestMethods(clazz)) {
					testReferences.add(new PatecaruTestReference(clazz, method.getName()));
				}
			} catch (Exception e) {
				// TODO: error handling
			}
		}
		return testReferences;
	}
	
	// this is JUnit 4 only... (no JUnit 3 support for now)
	private List<Method> getAllTestMethods(Class<?> clazz) {
		List<Method> testMethods = new ArrayList<Method>();
		for (Method m : clazz.getMethods()) {
			if (m.getAnnotation(Test.class) != null || m.getName().startsWith("test")) {
				testMethods.add(m);
			}
		}
		return testMethods;
	}
	
	/**
	 * Connect to a PatecaruBroker
	 */
	public void registerRunner(RunnerCallback callback) {
		this.testRunListener = new PatecaruRunListenerImpl(this, callback);
		PatecaruTestEnvironmentManager.getInstance().registerRunListener(this.testRunListener);
	}
	
	protected void init(String[] args) {
		for(int i= 0; i < args.length; i++) {
			if(args[i].toLowerCase().equals("-classnames") || args[i].toLowerCase().equals("-classname")){ //$NON-NLS-1$ //$NON-NLS-2$
				Vector<String> list= new Vector<String>();
				for (int j= i+1; j < args.length; j++) {
					if (args[j].startsWith("-"))
						break;
					list.add(args[j]);
				}
				testClassNames = list.toArray(new String[list.size()]);
			}
		}
	}
	
	public void printXMLResult() {
		StringBuffer b = new StringBuffer();
		b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		b.append("<testsuite workpackages=\""+this.workerPackageResultList.size()+"\">\n");
		
		for(PatecaruResult result : this.workerPackageResultList) {
			b.append("   <workpackage workerId=\""+result.getWorkerId()+"\">\n");
			
			for(PatecaruTestReference testRef : result.getResults().keySet()) {
				String className  = testRef.getTestClass().getName();
				String methodName = testRef.getTestMethod();
				Result testResult = result.getResults().get(testRef);
				b.append("     <testcase class=\""+className+"\" method=\""+methodName+"\" runtime=\""+testResult.getRunTime()+"\"");
				if(testResult.wasSuccessful()) {
					b.append("/>\n");
				} else {
					b.append(">\n");
					b.append("       <failures>\n");
					for(Failure failure : testResult.getFailures()) {
						String failureDescription = failure.getDescription().toString();
						String message = failure.getMessage();
						b.append("         <failure description=\""+failureDescription+"\" message=\""+message+"\">\n");
					}
					b.append("       </failures>\n");
					b.append("     </testcase>\n");
				}
			}
			b.append("   </workpackage>\n");
		}
		b.append("</testsuite>");
		System.out.println(b.toString());
	}
	
}
