package de.wwu.pi.patecaru.worker.jms;

import static de.wwu.pi.patecaru.configuration.PatecaruTestConfiguration.JMS_MESSAGE_BROKER_URL;
import static de.wwu.pi.patecaru.configuration.PatecaruTestConfiguration.JMS_QUEUE_RESULT;
import static de.wwu.pi.patecaru.configuration.PatecaruTestConfiguration.JMS_QUEUE_WORKPACKAGE;
import static de.wwu.pi.patecaru.configuration.PatecaruTestConfiguration.JMS_TOPIC_TESTRUN_START;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.runner.Result;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruResult;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruWorkpackage;
import de.wwu.pi.patecaru.datatypes.mq.WorkpackageRequest;
import de.wwu.pi.patecaru.worker.PatecaruTestWorker;
import de.wwu.pi.patecaru.worker.jms.listener.PatecaruTestRunStartedListener;
import de.wwu.pi.patecaru.worker.jms.listener.PatecaruWorkerResponseListener;

public class PatecaruTestJobClient {

	private static int ackMode;

	private boolean transacted = false;
	
	private MessageProducer producerWorkpackage;
	private MessageProducer producerResult;
	
	private Session session;
	
	private PatecaruTestWorker worker;

	public PatecaruTestWorker getWorker() {
		return this.worker;
	}
	
	static {
		ackMode = Session.AUTO_ACKNOWLEDGE;
	}
	
	public PatecaruTestJobClient(String workerId) {
		this.worker = new PatecaruTestWorker(workerId);
	}

	public void registerWorker() throws JMSException {
		Topic topic = this.session.createTopic(JMS_TOPIC_TESTRUN_START);
		MessageConsumer consumer = this.session.createConsumer(topic);
		consumer.setMessageListener(new PatecaruTestRunStartedListener(this));	
	}

	public void connect() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(JMS_MESSAGE_BROKER_URL);
		connectionFactory.setTrustAllPackages(true);
		Connection connection;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			this.session = connection.createSession(transacted, ackMode);
			
			Destination workpackageQueue = session.createQueue(JMS_QUEUE_WORKPACKAGE);
			this.producerWorkpackage = session.createProducer(workpackageQueue);
			this.producerWorkpackage.setDeliveryMode(DeliveryMode.NON_PERSISTENT);	
			
			Destination resultQueue = session.createQueue(JMS_QUEUE_RESULT);
			this.producerResult = session.createProducer(resultQueue);
			this.producerResult.setDeliveryMode(DeliveryMode.NON_PERSISTENT);	
		} catch (JMSException e) {
			// Handle the exception appropriately
			e.printStackTrace();
		}
	}

	public void sendWorkpackageRequest() {
		System.out.println("[WORKER-"+worker.getWorkerId()+"] Send a workpackage request...");
		WorkpackageRequest request = new WorkpackageRequest();
		request.setWorkerId(worker.getWorkerId());
		
		// set some runtime data of this worker
		Runtime rt = Runtime.getRuntime();
		request.setAvailableProcessors(rt.availableProcessors());
		request.setFreeMemory(rt.freeMemory());
		request.setMaxMemory(rt.maxMemory());
		sendJMSObjectMessage(this.producerWorkpackage, new PatecaruWorkerResponseListener(this), request);
	}
	
	public void sendTestRunFinished(PatecaruWorkpackage workpackage, Map<PatecaruTestReference, Result> results) {
		try {
			System.out.println("[WORKER-"+worker.getWorkerId()+"] Send a test run finished message...");
			PatecaruResult patecaruResult = new PatecaruResult(worker.getWorkerId(), results);
			ObjectMessage objMessage = session.createObjectMessage(patecaruResult);
			this.producerResult.send(objMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	private void sendJMSObjectMessage(MessageProducer producer, MessageListener responseListener, Serializable object2Send) {
		try {
			Destination tempDest = session.createTemporaryQueue();
			MessageConsumer responseConsumer = session.createConsumer(tempDest);
			responseConsumer.setMessageListener(responseListener);
						
			ObjectMessage objMessage = session.createObjectMessage(object2Send);
			objMessage.setJMSReplyTo(tempDest);
			
			String correlationId = this.createRandomString();
			objMessage.setJMSCorrelationID(correlationId);
            producer.send(objMessage);
		} catch (JMSException e) {
			// Handle the exception appropriately
			e.printStackTrace();
		}
	}
	
    private String createRandomString() {
        Random random = new Random(System.currentTimeMillis());
        long randomLong = random.nextLong();
        return Long.toHexString(randomLong);
    }
    
    public static void main(String[] args) throws JMSException {
    	String workerId = UUID.randomUUID().toString();
    	PatecaruTestJobClient client = new PatecaruTestJobClient(workerId);
    	client.connect();
    	client.registerWorker();
	}
}
