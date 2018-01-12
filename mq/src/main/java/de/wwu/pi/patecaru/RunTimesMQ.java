package de.wwu.pi.patecaru;

public class RunTimesMQ {

	private static long endConstrcutWorkPackageMaster;
	private static long startConstrcutWorkPackageMaster;
	
	public static void startConstrcutWorkPackageMaster() {
		startConstrcutWorkPackageMaster = System.currentTimeMillis();
	}
	
	public static void endConstrcutWorkPackageMaster() {
		endConstrcutWorkPackageMaster = System.currentTimeMillis();
	}
	
	public static void printTimes() {
		System.out.println("----------------------------------------");
		System.out.println("Start construct WPM:   " + startConstrcutWorkPackageMaster);
		System.out.println("End construct WPM:     " + endConstrcutWorkPackageMaster + " (" + (endConstrcutWorkPackageMaster - startConstrcutWorkPackageMaster) + ")");
		System.out.println("----------------------------------------");
	}
}
