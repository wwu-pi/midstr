package de.wwu.pi.patecaru.chunks;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Result;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruResult;
import de.wwu.pi.patecaru.datatypes.mq.PatecaruTestReference;
import de.wwu.pi.patecaru.db.DBManager;
import de.wwu.pi.patecaru.mq.listener.PatecaruRunListener;

// TODO persist into DB
public class ChunkHistoryKeeper implements PatecaruRunListener {

	private Map<Integer, Long> timePerTestRef = new HashMap<>();

	public Long getTime(PatecaruTestReference testRef) {
		return timePerTestRef.get(testRef.hashCode());
	}

	public void addNewTime(PatecaruTestReference testRef, Long time) {
		Long oldTime = timePerTestRef.get(testRef);
		if (oldTime != null) {
			time = (oldTime + time) / 2;
		}
		timePerTestRef.put(testRef.hashCode(), time);
	}

	public void clearTimes() {
		timePerTestRef.clear();
	}

	public void testWorkerPackageFinished(PatecaruResult patecaruResult) {
		Map<PatecaruTestReference, Result> resultMap = patecaruResult.getResults();
        for(PatecaruTestReference testRef : resultMap.keySet()) {
            addNewTime(testRef, resultMap.get(testRef).getRunTime());
        }
	}

	public void testRunStarted() {
		
	}

	public void testRunFinished() {
		DBManager dbManager = DBManager.getInstance();
		dbManager.startDB();
		for(Integer testRefCode : this.timePerTestRef.keySet()) {
			dbManager.storeTestResult(testRefCode, this.timePerTestRef.get(testRefCode));
		}
	}

	public void testWorkerPackageStarted(String workerId) {
	}
	
	private void loadTestResultsFromDatabase() {
		DBManager.getInstance().startDB();
		this.timePerTestRef  = DBManager.getInstance().loadTestResults();
	}

	private static ChunkHistoryKeeper hkSingleton;

	private ChunkHistoryKeeper() {
		loadTestResultsFromDatabase();
	}

	public static synchronized ChunkHistoryKeeper getInstance() {
		if (hkSingleton == null) {
			hkSingleton = new ChunkHistoryKeeper();
		}
		return hkSingleton;
	}
	
}
