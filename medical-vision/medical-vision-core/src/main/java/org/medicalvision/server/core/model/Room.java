package org.medicalvision.server.core.model;

import java.io.Serializable;

public class Room implements Serializable {

	private static final long serialVersionUID = 3937012424786512344L;
	private Patient patient;
	private int roomID;
	
	public Room() {}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	@Override
	public String toString() {
		return "Room [patient=" + patient + ", roomID=" + roomID + "]";
	}

}
