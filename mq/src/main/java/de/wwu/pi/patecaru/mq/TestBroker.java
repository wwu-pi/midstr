package de.wwu.pi.patecaru.mq;

import static de.wwu.pi.patecaru.configuration.PatecaruTestConfiguration.JMS_MESSAGE_BROKER_URL;
import static de.wwu.pi.patecaru.configuration.PatecaruTestConfiguration.JMS_QUEUE_RESULT;
import static de.wwu.pi.patecaru.configuration.PatecaruTestConfiguration.JMS_QUEUE_WORKPACKAGE;
import static de.wwu.pi.patecaru.configuration.PatecaruTestConfiguration.JMS_TOPIC_TESTRUN_START;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTopic;

import de.wwu.pi.patecaru.RunTimesMQ;
import de.wwu.pi.patecaru.chunks.ChunkHistoryKeeper;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruResult;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;
import de.wwu.pi.patecaru.mq.listener.PatecaruResultListener;
import de.wwu.pi.patecaru.mq.listener.PatecaruRunListener;
import de.wwu.pi.patecaru.mq.listener.PatecaruWorkpackageListener;

/**
 * 
 * @author Andreas Fuchs, Vincent von Hof
 *
 */
public class TestBroker extends AbstractWorkDistributer {

	private static int ackMode;

	protected Session session;
	protected Topic registerTopic;
	protected int registerTopicConsumerCount;
	private boolean transacted = false;

	private Collection<PatecaruRunListener> listeners = new HashSet<PatecaruRunListener>();

	private PatecaruWorkPackageMaster workPackageMaster;

	private PatecaruWorkpackageListener workpackageListener;
	private PatecaruResultListener resultListener;

	static {
		ackMode = Session.AUTO_ACKNOWLEDGE;
	}

	public void start() {
		try {
			startBrooker();
			startSession();
			setupRegisterTopic();
			setupWorkpackgeQueue();
			setupResultQueue();
			registerInternalListeners();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PatecaruWorkPackageMaster getWorkerPackageMaster() {
		return this.workPackageMaster;
	}

	private void startBrooker() throws Exception {
		BrokerService broker = new BrokerService();
		broker.setPersistent(false);
		broker.setUseJmx(false);
		broker.addConnector(JMS_MESSAGE_BROKER_URL);
		broker.start();
	}

	private void startSession() throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(JMS_MESSAGE_BROKER_URL);
		connectionFactory.setTrustAllPackages(true);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		this.session = connection.createSession(this.transacted, ackMode);
	}

	private void setupRegisterTopic() throws JMSException {
		registerTopic = session.createTopic(JMS_TOPIC_TESTRUN_START);
		ActiveMQTopic advisoryTopic = AdvisorySupport.getConsumerAdvisoryTopic(registerTopic);
		MessageConsumer consumer = this.session.createConsumer(advisoryTopic);
		consumer.setMessageListener(consumerCountListener);
	}

	private void setupWorkpackgeQueue() throws JMSException {
		Destination queue = this.session.createQueue(JMS_QUEUE_WORKPACKAGE);
		MessageConsumer consumer = this.session.createConsumer(queue);
		this.workpackageListener = new PatecaruWorkpackageListener(session);
		consumer.setMessageListener(workpackageListener);
	}

	private void setupResultQueue() throws JMSException {
		Destination queue = this.session.createQueue(JMS_QUEUE_RESULT);
		MessageConsumer consumer = this.session.createConsumer(queue);
		this.resultListener = new PatecaruResultListener(this);
		consumer.setMessageListener(resultListener);
	}

	private void sendTestRunStarted() throws JMSException {
		TextMessage message = this.session
				.createTextMessage("Dear worker, I would like to inform you to START THE TEST");
		MessageProducer producer = session.createProducer(registerTopic);
		producer.send(message);
	}

	private void registerInternalListeners() {
		addListener(ChunkHistoryKeeper.getInstance());
	}

	public void runTests(List<PatecaruTestReference> testReferences) throws Exception {
		// first, create the work package master
		// this master will create all work packages
		// TODO make PWPM singleton and only register it as listener once,
		// TODO also DESTROY it later!
		RunTimesMQ.startConstrcutWorkPackageMaster();
		this.workPackageMaster = new PatecaruWorkPackageMaster(testReferences, this.registerTopicConsumerCount);
		RunTimesMQ.endConstrcutWorkPackageMaster();
		addListener(this.workPackageMaster);
		this.workpackageListener.setWorkpackageMaster(this.workPackageMaster);
		// once the work packages are created, the test run can be started
		// i.e. a test notification is sent to the topic, such that all workers
		// are notified
		sendTestRunStarted();
	}

	public boolean addListener(PatecaruRunListener listener) {
		return listeners.add(listener);
	}

	public boolean removeLinster(PatecaruRunListener listener) {
		return listeners.remove(listener);
	}

	@Override
	public void notifyListeners(PatecaruResult result) {
		for (PatecaruRunListener listener : this.listeners) {
			listener.testWorkerPackageFinished(result);
		}
		if (this.workPackageMaster.isAllWorkDone()) {
			for (PatecaruRunListener listener : this.listeners) {
				listener.testRunFinished();
			}
		}
	}

	MessageListener consumerCountListener = new MessageListener() {

		public void onMessage(Message message) {
			if (message instanceof ActiveMQMessage) {
				try {
					ActiveMQMessage aMsg = (ActiveMQMessage) message;
					registerTopicConsumerCount = Integer.parseInt(aMsg.getStringProperty("consumerCount"));
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public static void main(String[] args) throws Exception {
		TestBroker broker = new TestBroker();
		broker.start();
		Thread.sleep(10000); // wait 10 seconds, so the workers have time to
								// subscribe on the topic
		broker.sendTestRunStarted();
	}
}