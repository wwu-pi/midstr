package de.wwu.pi.patecaru.mq.listener;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruResult;

/**
 * @author a_fuch05
 */
// TODO: umhaengen, das hier ist kein MQ listener, sondern allgmein ein runlistener
public interface PatecaruRunListener {

	/**
	 * The test run of all test work packages started.
	 */
	public void testRunStarted();
	
	/**
	 * The test run (=all test work packages) finished.
	 */
	public void testRunFinished();
	
	/**
	 * A worker has started to test a worker-package.
	 */
	public void testWorkerPackageStarted(String workerId);
	
	/**
	 * A worker has finished to test a worker-package.
	 * @param result the result, including the worker id
	 */
	public void testWorkerPackageFinished(PatecaruResult result);
	
}
