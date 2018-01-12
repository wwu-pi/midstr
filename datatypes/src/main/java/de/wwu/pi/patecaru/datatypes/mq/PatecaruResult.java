package de.wwu.pi.patecaru.datatypes.mq;

import java.io.Serializable;
import java.util.Map;

import org.junit.runner.Result;

import de.wwu.pi.patecaru.datatypes.mq.meta.WorkerMessage;

public class PatecaruResult implements Serializable, WorkerMessage {
	
	private static final long serialVersionUID = -2853506185159580937L;

	private Map<PatecaruTestReference, Result> results;
	private String workerId;
	
	public PatecaruResult(String workerId, Map<PatecaruTestReference, Result> results) {
		this.results = results;
		this.workerId = workerId;
	}

	public String getWorkerId() {
		return this.workerId;
	}
	
	public Map<PatecaruTestReference, Result> getResults() {
		return this.results;
	}

}
