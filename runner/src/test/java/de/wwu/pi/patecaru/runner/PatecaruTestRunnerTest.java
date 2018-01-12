package de.wwu.pi.patecaru.runner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.jms.JMSException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;
import de.wwu.pi.patecaru.env.PatecaruTestEnvironmentManager;
import de.wwu.pi.patecaru.mq.TestBroker;
import de.wwu.pi.patecaru.runner.PatecaruTestRunner;
import de.wwu.pi.patecaru.runner.listener.PatecaruRunListenerImpl;
import de.wwu.pi.patecaru.worker.jms.PatecaruTestJobClient;

public class PatecaruTestRunnerTest {

	// private PatecaruTestRunner runner;
	// private List<Thread> clientThreads;
	private int numClients = 2;

	@Before
	public void setUp() throws Exception {
		PatecaruTestEnvironmentManager.getInstance().setup(numClients);

		System.out.println("Wait one second for clients / workers to start...");
		Thread.sleep(1000);
	}

	@Test
	public void test() throws InterruptedException {
		try {
//			String[] args4allJSR116Classes = {"-classnames", "de.wwu.pi.jsr116.AbstractExecutorServiceTest", "de.wwu.pi.jsr116.AbstractQueuedLongSynchronizerTest", "de.wwu.pi.jsr116.AbstractQueuedSynchronizerTest", "de.wwu.pi.jsr116.AbstractQueueTest", "de.wwu.pi.jsr116.ArrayBlockingQueueTest", "de.wwu.pi.jsr116.ArrayDequeTest", "de.wwu.pi.jsr116.Atomic8Test", "de.wwu.pi.jsr116.AtomicBooleanTest", "de.wwu.pi.jsr116.AtomicIntegerArrayTest", "de.wwu.pi.jsr116.AtomicIntegerFieldUpdaterTest", "de.wwu.pi.jsr116.AtomicIntegerTest,AtomicLongArrayTest", "de.wwu.pi.jsr116.AtomicLongFieldUpdaterTest", "de.wwu.pi.jsr116.AtomicLongTest", "de.wwu.pi.jsr116.AtomicReferenceArrayTest", "de.wwu.pi.jsr116.AtomicReferenceFieldUpdaterTest", "de.wwu.pi.jsr116.AtomicReferenceTest", "de.wwu.pi.jsr116.AtomicStampedReferenceTest", "de.wwu.pi.jsr116.BlockingQueueTest", "de.wwu.pi.jsr116.Collection8Test", "de.wwu.pi.jsr116.CollectionImplementation", "de.wwu.pi.jsr116.CollectionTest", "de.wwu.pi.jsr116.ConcurrentHashMap8Test", "de.wwu.pi.jsr116.ConcurrentHashMapTest", "de.wwu.pi.jsr116.ConcurrentLinkedDequeTest", "de.wwu.pi.jsr116.ConcurrentLinkedQueueTest", "de.wwu.pi.jsr116.ConcurrentSkipListMapTest", "de.wwu.pi.jsr116.ConcurrentSkipListSetTest", "de.wwu.pi.jsr116.ConcurrentSkipListSubMapTest", "de.wwu.pi.jsr116.ConcurrentSkipListSubSetTest", "de.wwu.pi.jsr116.CopyOnWriteArrayListTest", "de.wwu.pi.jsr116.CopyOnWriteArraySetTest", "de.wwu.pi.jsr116.CountDownLatchTest", "de.wwu.pi.jsr116.CountedCompleterTest", "de.wwu.pi.jsr116.CyclicBarrierTest", "de.wwu.pi.jsr116.DelayQueueTest", "de.wwu.pi.jsr116.DoubleAccumulatorTest", "de.wwu.pi.jsr116.DoubleAdderTest", "de.wwu.pi.jsr116.EntryTest", "de.wwu.pi.jsr116.ExchangerTest", "de.wwu.pi.jsr116.ExecutorCompletionServiceTest", "de.wwu.pi.jsr116.ExecutorsTest", "de.wwu.pi.jsr116.ForkJoinPool8Test", "de.wwu.pi.jsr116.ForkJoinPoolTest,ForkJoinTaskTest", "de.wwu.pi.jsr116.FutureTaskTest", "de.wwu.pi.jsr116.JSR166TestCase", "de.wwu.pi.jsr116.LinkedBlockingDequeTest", "de.wwu.pi.jsr116.LinkedBlockingQueueTest", "de.wwu.pi.jsr116.LinkedListTest", "de.wwu.pi.jsr116.LinkedTransferQueueTest", "de.wwu.pi.jsr116.LockSupportTest", "de.wwu.pi.jsr116.LongAccumulatorTest", "de.wwu.pi.jsr116.LongAdderTest", "de.wwu.pi.jsr116.PhaserTest", "de.wwu.pi.jsr116.PriorityBlockingQueueTest,PriorityQueueTest", "de.wwu.pi.jsr116.RecursiveActionTest", "de.wwu.pi.jsr116.RecursiveTaskTest", "de.wwu.pi.jsr116.ReentrantLockTest", "de.wwu.pi.jsr116.ReentrantReadWriteLockTest", "de.wwu.pi.jsr116.ScheduledExecutorSubclassTest", "de.wwu.pi.jsr116.ScheduledExecutorTest", "de.wwu.pi.jsr116.SemaphoreTest", "de.wwu.pi.jsr116.SplittableRandomTest", "de.wwu.pi.jsr116.StampedLockTest", "de.wwu.pi.jsr116.SynchronousQueueTest", "de.wwu.pi.jsr116.SystemTest", "de.wwu.pi.jsr116.ThreadLocalRandom8Test", "de.wwu.pi.jsr116.ThreadLocalRandomTest", "de.wwu.pi.jsr116.ThreadLocalTest", "de.wwu.pi.jsr116.ThreadPoolExecutorSubclassTest", "de.wwu.pi.jsr116.ThreadPoolExecutorTest", "de.wwu.pi.jsr116.ThreadTest", "de.wwu.pi.jsr116.TimeUnitTest", "de.wwu.pi.jsr116.TreeMapTest", "de.wwu.pi.jsr116.TreeSetTest", "de.wwu.pi.jsr116.TreeSubMapTest", "de.wwu.pi.jsr116.TreeSubSetTest"};
			String[] args4allJSR116Classes = {"-classnames", "de.wwu.pi.jsr116.AbstractExecutorServiceTest", "de.wwu.pi.jsr116.AbstractQueuedLongSynchronizerTest", "de.wwu.pi.jsr116.AbstractQueuedSynchronizerTest", "de.wwu.pi.jsr116.AbstractQueueTest", "de.wwu.pi.jsr116.ArrayBlockingQueueTest", "de.wwu.pi.jsr116.ArrayDequeTest", "de.wwu.pi.jsr116.Atomic8Test", "de.wwu.pi.jsr116.AtomicBooleanTest", "de.wwu.pi.jsr116.AtomicIntegerArrayTest", "de.wwu.pi.jsr116.AtomicIntegerFieldUpdaterTest"};
			String[] args = {"-classnames", "de.wwu.pi.patecaru.sampletests.TestCaseNoOne", "de.wwu.pi.patecaru.sampletests.TestCaseNoTwo"};
			String[] longRunTestsArgs = {"-classnames", 
					"de.wwu.pi.patecaru.sampletests.longtests.TestNoOne", 
					"de.wwu.pi.patecaru.sampletests.longtests.TestNoTwo",
					"de.wwu.pi.patecaru.sampletests.longtests.TestNoThree",
					"de.wwu.pi.patecaru.sampletests.longtests.TestNoFour",
					"de.wwu.pi.patecaru.sampletests.longtests.TestNoFive"};
			PatecaruTestRunner.main(longRunTestsArgs);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// System.out.println("Wait 10 seconds for workers to finish their
		// work... ");
		// Thread.sleep(10000);
		// System.out.println("Thank you for testing! Shut down now! Good
		// bye...");
		// TODO handle Threads not being able to connect
	}

}
