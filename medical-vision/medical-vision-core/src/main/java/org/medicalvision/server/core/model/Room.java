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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((patient == null) ? 0 : patient.hashCode());
		result = prime * result + roomID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (patient == null) {
			if (other.patient != null)
				return false;
		} else if (!patient.equals(other.patient))
			return false;
		if (roomID != other.roomID)
			return false;
		return true;
	}

}
