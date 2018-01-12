package de.wwu.pi.patecaru.worker.jms.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import de.wwu.pi.patecaru.worker.jms.PatecaruTestJobClient;

/**
 * @author a_fuch05
 */
public class PatecaruTestRunStartedListener implements MessageListener {
	
	private PatecaruTestJobClient client;
	
	public PatecaruTestRunStartedListener(PatecaruTestJobClient client) {
		this.client = client;
	}

	public void onMessage(Message message) {
		System.out.println("[WORKER-"+client.getWorker().getWorkerId()+"] I got the message to start the test run... Ok, I'll do it...");
		this.client.sendWorkpackageRequest();
	}

}
