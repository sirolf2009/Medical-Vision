package org.medicalvision.server.core.model;

public class Room {

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

}
