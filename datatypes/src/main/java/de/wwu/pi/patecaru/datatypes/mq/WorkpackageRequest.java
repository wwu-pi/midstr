package de.wwu.pi.patecaru.datatypes.mq;

import java.io.Serializable;

import de.wwu.pi.patecaru.datatypes.mq.meta.WorkerMessage;

/**
 * @author a_fuch05
 */
public class WorkpackageRequest implements Serializable, WorkerMessage {

	private static final long serialVersionUID = -3726499826279071135L;

	private String workerId;
	private int availableProcessors;
	private long freeMemory;
	private long maxMemory;
	
	// TODO: ein worker koennte noch ein paar leistungsdaten mitschicken, z.B. CPU power usw...
	
	public WorkpackageRequest() {
	}
	
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	
	public String getWorkerId() {
		return this.workerId;
	}

	public int getAvailableProcessors() {
		return availableProcessors;
	}

	public void setAvailableProcessors(int availableProcessors) {
		this.availableProcessors = availableProcessors;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}
	
	@Override
	public String toString() {
		return "workerId=" + workerId + ", availableProcessors=" + availableProcessors + ", memory="+freeMemory+"/"+maxMemory;
	}
}