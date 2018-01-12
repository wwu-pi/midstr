package de.wwu.pi.patecaru.datatypes.mq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatecaruWorkpackage implements Serializable {

	private static final long serialVersionUID = 2112996678321286112L;
	
	private List<PatecaruTestReference> testCaseReferenceList;

	public PatecaruWorkpackage() {
		this.testCaseReferenceList = new ArrayList<PatecaruTestReference>();
	}
	
	public List<PatecaruTestReference> getTestCaseReferences() {
		return this.testCaseReferenceList;
	}

	public void addTestCaseReference(PatecaruTestReference patecaruTestReference) {
		this.testCaseReferenceList.add(patecaruTestReference);
	}	
	
}
