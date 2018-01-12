package de.wwu.pi.patecaru.worker.listener;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruWorkpackage;
import de.wwu.pi.patecaru.worker.jms.PatecaruTestJobClient;

public class PatecaruWorkerRunListener {

	private PatecaruTestJobClient client;
	private PatecaruWorkpackage workpackage;
	
	public PatecaruWorkerRunListener(PatecaruTestJobClient client, PatecaruWorkpackage workpackage) {
		this.client = client;
		this.workpackage = workpackage;
	}

	public void startWorkpackageTest() {
		
	}

	public void finishedWorkpackageTest(Map<PatecaruTestReference, Result> testResults) {
		System.out.println("Workpackage " + workpackage + " finished.");
		client.sendTestRunFinished(workpackage, testResults);
		client.sendWorkpackageRequest();
	}
	
	
	
	
	
	
	
	
	
	
	
//	@Override
//	public void testRunStarted(Description description) throws Exception {
//		startTime.set(System.currentTimeMillis());
//	}
//	
//	@Override
//	public void testStarted(Description description) throws Exception {
//		super.testStarted(description);
//	}
//		
//	@Override
//	public void testRunFinished(Result result) throws Exception {
//		long endTime = System.currentTimeMillis();
//		runTime.addAndGet(endTime - startTime.get());
//		
//		System.out.println("######## RUN TIME: " + runTime.get() + "ms");
//		
//		client.sendTestRunFinished(result, workpackage);
//		client.sendWorkpackageRequest();
//		super.testRunFinished(result);
//	}
}
