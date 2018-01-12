package de.wwu.pi.patecaru.env.worker;

import java.util.UUID;

import javax.jms.JMSException;

import de.wwu.pi.patecaru.worker.jms.PatecaruTestJobClient;

public class WorkerRunable implements Runnable {

	public void run() {
		String workerId = UUID.randomUUID().toString();
    	PatecaruTestJobClient client = new PatecaruTestJobClient(workerId);
    	client.connect();
    	try {
			client.registerWorker();
		} catch (JMSException e) {
			System.out.println("[WORKER] Error while registering worker...");
			e.printStackTrace();
		}
	}

}
