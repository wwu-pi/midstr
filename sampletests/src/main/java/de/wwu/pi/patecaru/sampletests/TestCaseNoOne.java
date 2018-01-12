package de.wwu.pi.patecaru.sampletests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class TestCaseNoOne {
	
	private static int numberRuns = 0;
	
	@BeforeClass
	public static void beforeClass() {
		List<String> lines = Arrays.asList("foo");
		Path file = Paths.get("C:/tmp/patecaru/beforeclass-"+numberRuns+"-"+UUID.randomUUID().toString()+".txt");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void afterClass() {
		List<String> lines = Arrays.asList("foo");
		Path file = Paths.get("C:/tmp/patecaru/afterclass-"+numberRuns+"-"+UUID.randomUUID().toString()+".txt");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void before() {
		List<String> lines = Arrays.asList("foo");
		Path file = Paths.get("C:/tmp/patecaru/before-"+numberRuns+"-"+UUID.randomUUID().toString()+".txt");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void after() {
		List<String> lines = Arrays.asList("foo");
		Path file = Paths.get("C:/tmp/patecaru/after-"+numberRuns+"-"+UUID.randomUUID().toString()+".txt");
		numberRuns++;
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void thisIsTestNo1() {
		Assert.assertTrue(5 > 2);
	}
	
	@Test(expected = ArithmeticException.class)
	public void testArithmetic() {
		throw new ArithmeticException("Das hier ist komplett falsch, 0 kann man nicht teilen");
	}
	
	@Test(timeout=3000)
	public void thisIsTestNo2() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(3 < 5);
	}
}
