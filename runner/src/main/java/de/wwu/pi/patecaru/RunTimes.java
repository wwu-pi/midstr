package de.wwu.pi.patecaru;

public class RunTimes {

	private static long startRun;
	private static long endRun;
	private static long endTestRun;
	private static long endSetupEnvironment;
	private static long startTestRun;
	private static long endRegisterRunners;
	private static long startStartTestBroker;
	private static long endStartTestBroker;
	
	public static void endRegisterRunners() {
		endRegisterRunners = System.currentTimeMillis();
	}
	
	public static void startRun() {
		startRun = System.currentTimeMillis();
	}
	
	public static void startTestRun() {
		startTestRun = System.currentTimeMillis();
	}
	
	public static void endRun() {
		endRun = System.currentTimeMillis();
	}
	
	public static void endTestRun() {
		endTestRun = System.currentTimeMillis();
	}

	public static void endSetupEnvironment() {
		endSetupEnvironment = System.currentTimeMillis();
	}
	
	public static void printTimes() {
		System.out.println("----------------------------------------");
		System.out.println("Start run:             " + startRun);
		System.out.println("Start test broker:     " + startStartTestBroker);
		System.out.println("End start test broker: " + endStartTestBroker + " (" + (endStartTestBroker-startStartTestBroker) + ")");
		System.out.println("End setup environment: " + endSetupEnvironment + "(" + (endSetupEnvironment-startRun) + ")");
		System.out.println("End register runners:  " + endRegisterRunners  + "(" + (endRegisterRunners-endSetupEnvironment) + ")");
		System.out.println("Start test run:        " + startTestRun        + "(" + (startTestRun-endRegisterRunners) + ")");
		System.out.println("End test run:          " + endTestRun          + "(" + (endTestRun-startTestRun) + ")");
		System.out.println("End run:               " + endRun              + "(" + (endRun-endTestRun) + ")");
		System.out.println("----------------------------------------");
	}

	public static void startStartTestBroker() {
		startStartTestBroker = System.currentTimeMillis();
	}
	
	public static void endStartTestBroker() {
		endStartTestBroker = System.currentTimeMillis();
	}
}
