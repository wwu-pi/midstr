package de.wwu.pi.patecaru.configuration;

public class PatecaruTestConfiguration {
	
	/**
	 * the URL of the master message JMS broker
	 */
	public static final String JMS_MESSAGE_BROKER_URL = "tcp://127.0.0.1:8080";
	
	/**
	 * the name of the work package JMS queue
	 */
	public static final String JMS_QUEUE_WORKPACKAGE = "patecaru.queue.workpackage";
	
	/**
	 * the name of the result JMS queue
	 */
	public static final String JMS_QUEUE_RESULT = "patecaru.queue.result";
	
	/**
	 * the name of the JMS topic on which the workers can subscribe in order to get informed when a test run starts
	 */
	public static final String JMS_TOPIC_TESTRUN_START = "patecaru.queue.testrun.start";
	
}
