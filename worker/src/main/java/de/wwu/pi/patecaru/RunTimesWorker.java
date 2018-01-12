package de.wwu.pi.patecaru;

import java.util.HashMap;
import java.util.Map;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;

public class RunTimesWorker {

	private static long endWorking;
	private static long startWorking;
	private static Map<PatecaruTestReference, Long> startTestRefMap = new HashMap<PatecaruTestReference, Long>();
	private static Map<PatecaruTestReference, Long> endTestRefMap = new HashMap<PatecaruTestReference, Long>();

	public static void startWorking() {
		startWorking = System.currentTimeMillis();
	}

	public static void endWorking() {
		endWorking = System.currentTimeMillis();
	}

	public static void startWorkOnTestReference(PatecaruTestReference testReference) {
		startTestRefMap.put(testReference, System.currentTimeMillis());
	}
	
	public static void endWorkOnTestReference(PatecaruTestReference testReference) {
		endTestRefMap.put(testReference, System.currentTimeMillis());
	}
	
	public static void printTimes() {
		System.out.println("----------------------------------------");
		System.out.println("Start working          " + startWorking);
		long sum = 0;
		for(PatecaruTestReference tr : startTestRefMap.keySet()) {
			Long start = startTestRefMap.get(tr);
			Long end   = endTestRefMap.get(tr);
			if(tr == null || start == null || end == null) { continue; }
			sum = end - start + sum;
			System.out.println(" Test Reference:   class=" + tr.getTestClass().getSimpleName() + ", method=" + tr.getTestMethod() + ", start=" + start + ", end=" + end + ", diff=" + (end-start));
		}
		System.out.println("    SUM= " + sum);
		System.out.println("End working           " + endWorking   +  " (" + (endWorking-startWorking) + ")");
		System.out.println("----------------------------------------");
	}
}
