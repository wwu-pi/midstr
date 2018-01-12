package de.wwu.pi.patecaru.runner.listener;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruResult;
import de.wwu.pi.patecaru.mq.listener.PatecaruRunListener;
import de.wwu.pi.patecaru.runner.PatecaruTestRunner;
import de.wwu.pi.patecaru.runner.callback.RunnerCallback;

public class PatecaruRunListenerImpl implements PatecaruRunListener {

	private PatecaruTestRunner runner;
	private RunnerCallback callback;
	
	public PatecaruRunListenerImpl(PatecaruTestRunner runner, RunnerCallback callback) {
		this.runner = runner;
		this.callback = callback;
	}
	
	public void testRunStarted() {
	}

	public void testRunFinished() {
		callback.callback();
	}

	public void testWorkerPackageStarted(String workerId) {
		System.out.println("Worker [" + workerId +"] starts testing...");
	}

	public void testWorkerPackageFinished(PatecaruResult result) {
		System.out.println("testing a worker package finished");
		this.runner.addResult(result);
	}

}
