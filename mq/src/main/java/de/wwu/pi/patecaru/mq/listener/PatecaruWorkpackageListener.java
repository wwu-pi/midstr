package de.wwu.pi.patecaru.mq.listener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import de.wwu.pi.patecaru.datatypes.mq.WorkpackageRequest;
import de.wwu.pi.patecaru.mq.listener.meta.SendWorkpackageRequest;

/**
 * @author a_fuch05
 */
public class PatecaruWorkpackageListener extends SendWorkpackageRequest {
	
	public PatecaruWorkpackageListener(Session session) {
		super(session);
	}

	@Override
	protected void handleObjectMessage(ObjectMessage objectMessage) throws JMSException {
		WorkpackageRequest request = (WorkpackageRequest) objectMessage.getObject();
		System.out.println("[BROKER-PatecaruWorkpackageListener-"+this.hashCode()+"] got a request from worker: " + request);
	}
}
