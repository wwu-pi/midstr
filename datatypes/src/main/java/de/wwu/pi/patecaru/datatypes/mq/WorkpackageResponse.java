package de.wwu.pi.patecaru.datatypes.mq;

import java.io.Serializable;

/**
 * @author a_fuch05
 */
public class WorkpackageResponse implements Serializable {

	private static final long serialVersionUID = -8528407370116053133L;

	private String workToDo; // TODO: austauschen mit echten test reference
	private PatecaruWorkpackage workpackage;
	
	public WorkpackageResponse() {
	}

	public String getWorkToDo() {
		return workToDo;
	}

	public void setWorkToDo(String workToDo) {
		this.workToDo = workToDo;
	}

	public PatecaruWorkpackage getWorkpackage() {
		return workpackage;
	}

	public void setWorkpackage(PatecaruWorkpackage workpackage) {
		this.workpackage = workpackage;
	}
}
