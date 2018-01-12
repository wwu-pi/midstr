package de.wwu.pi.gen;

import java.util.concurrent.ThreadLocalRandom;

public class TestCaseGenerator {

	public static void main(String[] args) {
		String className = "TestNoFour";
		int maxTestMethods = 100;
		int minWaitTime = 10;
		int maxWaitTime = 100;

		
		
		String[] methods = new String[maxTestMethods];
		int allWaiting = 0;
		for(int i=0; i<maxTestMethods; i++) {
			int r = randInt(minWaitTime, maxWaitTime);
			allWaiting += r;
			methods[i] = genTestCase("myTM_"+i+"_wait_"+r, r);
		}
		
		System.out.println("/**");
		System.out.println("  * test methods in this generated class: " + maxTestMethods);
		System.out.println("  * complete \"wait\" time: " + allWaiting);
		System.out.println("  */");
		System.out.println("public class " + className + " {");
		for(String method : methods) {
			System.out.println(method);
		}
		System.out.println("}");		
	}
	
	public static String genTestCase(String testMethodName, long waitMillis) {
		String method = "";
		method += "@Test\n";
		method += "public void " + testMethodName + "() {\n";
		method += "try {\n";
		method += "Thread.sleep("+waitMillis+");\n";
		method += "} catch (InterruptedException e) {\n";
		method += "e.printStackTrace();\n";
		method += "}\n";
		method += "}\n";
		return method;
	}
	
	public static int randInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
}
