package org.medicalvision.server.core.model;

public class Patient extends Person {
	
	private String bsn;

	public String getBsn() {
		return bsn;
	}

	public void setBsn(String bsn) {
		this.bsn = bsn;
	}

}
