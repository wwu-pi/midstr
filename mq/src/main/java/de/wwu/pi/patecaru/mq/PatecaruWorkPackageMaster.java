package de.wwu.pi.patecaru.mq;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.wwu.pi.patecaru.RunTimesMQ;
import de.wwu.pi.patecaru.chunks.ChunkHistoryKeeper;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruResult;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruWorkpackage;
import de.wwu.pi.patecaru.mq.listener.PatecaruRunListener;

public class PatecaruWorkPackageMaster implements PatecaruRunListener {

	private Collection<PatecaruWorkpackage> workpackages;
	private Map<String, PatecaruWorkpackage> workerOnWorkpackages;

	/**
	 * @param testCaseReferences
	 *            all test case references that should be distributed to the
	 *            workers
	 */
	public PatecaruWorkPackageMaster(List<PatecaruTestReference> testCaseReferences, int workers) {
		this.workpackages = new LinkedList<PatecaruWorkpackage>();
		this.workerOnWorkpackages = new HashMap<String, PatecaruWorkpackage>();
		this.buildWorkPackages(testCaseReferences, workers);
	}

	/**
	 * Builds workpackages ("partitions"). May rely upon knowledge of past
	 * executions.
	 * 
	 * @param testCaseReferences
	 *            The methods under test
	 * @param workers
	 *            Number of workers currently available as execution targets
	 */
	private void buildWorkPackages(List<PatecaruTestReference> testCaseReferences, int workers) {
		workpackages.clear();
		// partition for number of workers
		// TODO 0: n refs -> n WP
		// TODO 1: m workers -> m WP
		// TODO 2: uniform packages based upon execution time history
		// CASE 1
		int strategy = 1;
		switch (strategy) {
		case 0: {
			for (PatecaruTestReference tr : testCaseReferences) {
				PatecaruWorkpackage workPackage = new PatecaruWorkpackage();
				workPackage.addTestCaseReference(tr);
				workpackages.add(workPackage);
			}
		}
		case 1: {
			// initialize n work packages
			PatecaruWorkpackage[] workPackagesInConstruction = new PatecaruWorkpackage[workers];
			for(int i=0; i<workers; i++) {
				PatecaruWorkpackage workPackage = new PatecaruWorkpackage();
				workPackagesInConstruction[i] = workPackage;
				workpackages.add(workPackage);
			}
			Iterator<PatecaruTestReference> tcIt = testCaseReferences.iterator();
			int i=0;
			while (tcIt.hasNext()) {
				workPackagesInConstruction[i].addTestCaseReference(tcIt.next());
				i = (i+1)%workers;
			}
		}
		case 2: {
		}
		}
	}

	/**
	 * Check if there are no more worker packages left <i>and</i> if all workers
	 * have finished their work.
	 */
	public boolean isAllWorkDone() {
		return workpackages.size() == 0 && workerOnWorkpackages.size() == 0;
	}

	/**
	 * Check if there are any worker packages left (that are not assigned to any
	 * worker yet).
	 */
	public boolean isStillWorkToDo() {
		return this.workpackages.size() > 0;
	}

	/**
	 * Returns the next work package in the list, or <code>null</code> if no
	 * work package left.
	 */
	public PatecaruWorkpackage getNextWorkpackage(String workerId) {
		if (this.workpackages != null) {
			Iterator<PatecaruWorkpackage> i = this.workpackages.iterator();
			if (i.hasNext()) {
				// first, remove work package from collection
				PatecaruWorkpackage wp = i.next();
				i.remove();

				// second, add work package on 'working' map
				this.workerOnWorkpackages.put(workerId, wp);

				return wp;
			}
		}
		return null;
	}

	public void testWorkerPackageFinished(PatecaruResult result) {
		System.out.println("######################################");
		System.out.println("##### Size of 'onWorker'-map: " + this.workerOnWorkpackages.size());
		System.out.println("######################################");
		this.workerOnWorkpackages.remove(result.getWorkerId());
	}

	public void testRunStarted() {
		// not interesting for the test package master...
	}

	public void testRunFinished() {
		// not interesting for the test package master...
		
		// TODO delete this... it is just for analyzing the run times..
		System.out.println("### RUN TIMES FROM THE MQ PROJECT ##");
		RunTimesMQ.printTimes();
		System.out.println("####################################");
	}

	public void testWorkerPackageStarted(String workerId) {
		// not interesting for the test package master...
	}
}
