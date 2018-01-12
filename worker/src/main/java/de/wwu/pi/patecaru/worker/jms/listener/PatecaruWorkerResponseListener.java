package de.wwu.pi.patecaru.worker.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import de.wwu.pi.patecaru.RunTimesWorker;
import de.wwu.pi.patecaru.datatypes.mq.WorkpackageResponse;
import de.wwu.pi.patecaru.worker.PatecaruTestWorker;
import de.wwu.pi.patecaru.worker.jms.PatecaruTestJobClient;
import de.wwu.pi.patecaru.worker.listener.PatecaruWorkerRunListener;

/**
 * @author a_fuch05
 */
public class PatecaruWorkerResponseListener implements MessageListener {
	
	private PatecaruTestJobClient client;
	
	public PatecaruWorkerResponseListener(PatecaruTestJobClient client) {
		this.client = client;
	}

	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				ObjectMessage objMsg = (ObjectMessage) message;
				WorkpackageResponse response = (WorkpackageResponse) objMsg.getObject();
				System.out.println("[WORKER-"+client.getWorker().getWorkerId()+"] got a response to work on: " + response.getWorkToDo());
				
				new PatecaruTestWorker(client.getWorker().getWorkerId()).work(response.getWorkpackage(), 
						new PatecaruWorkerRunListener(this.client, response.getWorkpackage()));
			}
		} catch (JMSException e) {
			e.printStackTrace();
			// Handle the exception appropriately
		}
		
		System.out.println("### RUN TIMES WORKER ###");
		RunTimesWorker.printTimes();
		System.out.println("########################");
	}

}
