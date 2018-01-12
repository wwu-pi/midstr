package de.wwu.pi.patecaru.sampletests;

import org.junit.Assert;
import org.junit.Test;


public class TestCaseNoTwo {

	@Test
	public void thisIsTestNo1InTwo() {
		Assert.assertTrue("Test: fünf ist größer als zwei", 5 < 2);
	}
	
	@Test
	public void thisIsTestNo2InTwo() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(3 < 5);
	}
}
