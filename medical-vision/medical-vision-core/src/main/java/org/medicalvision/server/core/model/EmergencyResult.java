package org.medicalvision.server.core.model;

import java.io.Serializable;

public class EmergencyResult implements Serializable {
	
	private static final long serialVersionUID = 2699304343366608528L;

	private long ID;
	private boolean actuallyAnEmergency;
	
	public EmergencyResult() {}
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public boolean isActuallyAnEmergency() {
		return actuallyAnEmergency;
	}
	public void setActuallyAnEmergency(boolean actuallyAnEmergency) {
		this.actuallyAnEmergency = actuallyAnEmergency;
	}

}
