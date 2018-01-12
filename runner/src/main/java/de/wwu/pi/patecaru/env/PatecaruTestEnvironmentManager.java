package de.wwu.pi.patecaru.env;

import java.util.ArrayList;
import java.util.Collection;

import de.wwu.pi.patecaru.RunTimes;
import de.wwu.pi.patecaru.env.worker.WorkerRunable;
import de.wwu.pi.patecaru.mq.TestBroker;
import de.wwu.pi.patecaru.mq.listener.PatecaruRunListener;

/**
 * This class manages the test environment, e.g. can start the test broker and
 * some workers.
 */
public class PatecaruTestEnvironmentManager {

	private static PatecaruTestEnvironmentManager instance = null;

	private Collection<Thread> testWorkerThreads;
	private TestBroker testBroker;
	
	private boolean isSetup;
	

	public void setup(int numberOfWorkerThreads) {
		startTestBroker();
		startTestWorkersInThread(numberOfWorkerThreads);
		isSetup = true;
	}

	/**
	 * Start the test broker, and return the started test broker.
	 */
	public void startTestBroker() {
		RunTimes.startStartTestBroker();
		this.testBroker = new TestBroker();
		this.testBroker.start();
		RunTimes.endStartTestBroker();
	}
	
	public void registerRunListener(PatecaruRunListener... listeners) {
		for (PatecaruRunListener listener : listeners) {
			this.testBroker.addListener(listener);
		}
	}

	/**
	 * Start test workers in own thread.
	 * 
	 * @param testWorkerAmount
	 *            the amount of test workers to start
	 */
	public void startTestWorkersInThread(int testWorkerAmount) {
		for (int i = 0; i < testWorkerAmount; i++) {
			Thread testWorkerThread = new Thread(new WorkerRunable());
			this.testWorkerThreads.add(testWorkerThread);
			testWorkerThread.start();
		}
	}

	/**
	 * Stop all running worker threads.
	 */
	public void shutdownTestEnvironment() {
		for(Thread workerThread : this.testWorkerThreads) {
			try {
				workerThread.interrupt();
			} catch(Exception e) {
				System.out.println("Error while stopping worker thread: " + e.getMessage());
			}
		}
		isSetup = false;
	}
	
	public boolean isSetup() {
		return isSetup;
	}
	
	private PatecaruTestEnvironmentManager() {
		this.testWorkerThreads = new ArrayList<Thread>();
	}

	/**
	 * Get the singleton instance of this class.
	 */
	public static synchronized PatecaruTestEnvironmentManager getInstance() {
		if (instance == null) {
			instance = new PatecaruTestEnvironmentManager();
		}
		return instance;
	}

	public TestBroker getTestBroker() {
		return this.testBroker;
	}
}
