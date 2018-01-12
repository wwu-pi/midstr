package de.wwu.pi.patecaru.mq.listener.meta;

import java.io.Serializable;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import de.wwu.pi.patecaru.datatypes.mq.WorkpackageResponse;
import de.wwu.pi.patecaru.datatypes.mq.meta.WorkerMessage;
import de.wwu.pi.patecaru.mq.PatecaruWorkPackageMaster;

/**
 * @author a_fuch05
 */
public abstract class SendWorkpackageRequest implements MessageListener {
	
	protected Session session;
	protected PatecaruWorkPackageMaster workPackageMaster;
	
	private String className;
	
	public SendWorkpackageRequest(Session session) {
		this.session = session;
		this.className = this.getClass().getSimpleName();
	}
	
	public void setWorkpackageMaster(PatecaruWorkPackageMaster workPackageMaster) {
		this.workPackageMaster = workPackageMaster;
	}
	
	public void onMessage(Message message) {
		System.out.println("[BROKER-"+className+"-"+this.hashCode()+"] Received a message from a worker. Will send a workpackage back to the worker (if there exists one)");
		try {
			if (message instanceof ObjectMessage) {
				System.out.println("[BROKER-"+className+"-"+this.hashCode()+"] Message is an object message, handle it...");
				ObjectMessage objectMessage = (ObjectMessage) message;
				handleObjectMessage(objectMessage);
				System.out.println("[BROKER-"+className+"-"+this.hashCode()+"] ... message handled!");
				if(objectMessage.getObject() instanceof WorkerMessage) {
					String workerId = ((WorkerMessage)objectMessage.getObject()).getWorkerId();
					System.out.println("[BROKER-"+className+"-"+this.hashCode()+"] Message is sent by a worker : " + workerId + "... Sent a workpackage back to it");
					boolean sent = sendWorkpackageResponse(message.getJMSReplyTo(), message.getJMSCorrelationID(), workerId);
					System.out.println("[BROKER-"+className+"-"+this.hashCode()+"] Workpackage sent to worker: " + sent);
				} else {
					System.out.println("[BROKER-"+className+"-"+this.hashCode()+"] Object in the message is not of type WorkerMessage, cannot send new workpackage to unknown worker.");
				}				
			}
		} catch (JMSException e) {
			e.printStackTrace();
			// Handle the exception appropriately
		}
	}
	
	/**
	 * Abstract method to handle the object message send by a worker. <br>
	 * The message can be a workpackage-request, a result-message, etc.
	 */
	protected abstract void handleObjectMessage(ObjectMessage objectMessage) throws JMSException;

	protected boolean sendWorkpackageResponse(Destination destination, String correlationId, String workerId) throws JMSException {
		if(this.workPackageMaster.isStillWorkToDo()) {
			System.out.println("[BROKER-"+className+"-"+this.hashCode()+"] Still work to do, send a workpackage to worker: " + workerId);
			ObjectMessage response = this.session.createObjectMessage();
			response.setObject(getWorkpackageForWorker(workerId));
			response.setJMSCorrelationID(correlationId);
			
			MessageProducer replyProducer = this.session.createProducer(null);
			replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			replyProducer.send(destination, response);
			
			return true;
		}
		System.out.println("[BROKER-"+className+"-"+this.hashCode()+"] No more work to do...");
		return false;
	}
	
	private Serializable getWorkpackageForWorker(String workerId) {
		WorkpackageResponse response = new WorkpackageResponse();
		response.setWorkToDo("Hallo Worker "+ workerId + ", bitte mach tolle arbeit!");
		response.setWorkpackage(this.workPackageMaster.getNextWorkpackage(workerId));
		return response;
	}
}
