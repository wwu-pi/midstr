package de.wwu.pi.patecaru.mq.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruResult;
import de.wwu.pi.patecaru.mq.TestBroker;

/**
 * @author a_fuch05
 */
public class PatecaruResultListener implements MessageListener {

	private TestBroker testBroker;

	public PatecaruResultListener(TestBroker testBroker) {
		this.testBroker = testBroker;
	}

	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			try {
				ObjectMessage objMsg = (ObjectMessage) message;
				Object obj = objMsg.getObject();
				if (obj instanceof PatecaruResult) {
					// first, get the result object
					PatecaruResult result = (PatecaruResult) obj;
					// second, inform the listeners on the test broker
					testBroker.notifyListeners(result);
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
