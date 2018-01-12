package mq;

import org.junit.Assert;
import org.junit.Test;

import de.wwu.pi.patecaru.chunks.ChunkHistoryKeeper;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;

public class ChunkHistoryKeeperTest {

	@Test
	public void test() {
		PatecaruTestReference testReference = new PatecaruTestReference(getClass(), "test");
		ChunkHistoryKeeper.getInstance().addNewTime(testReference, 2000l);
		Assert.assertTrue(ChunkHistoryKeeper.getInstance().getTime(testReference) == 2000l);
	}
	
	/**
	 * Test if PatecaruTestreference stemming from the same CLAZZ and METHOD are seen as the same object in our HashMap
	 */
	@Test
	public void testHashs() {
		PatecaruTestReference testReference = new PatecaruTestReference(getClass(), "test");
		PatecaruTestReference testReferenceSame = new PatecaruTestReference(getClass(), "test");
		ChunkHistoryKeeper.getInstance().addNewTime(testReference, 2000l);
		Assert.assertTrue(ChunkHistoryKeeper.getInstance().getTime(testReferenceSame) == 2000l);
	}


}
