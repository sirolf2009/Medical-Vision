package org.medicalvision.server.core.model;

import java.io.Serializable;

public class Task implements Serializable {

	private static final long serialVersionUID = 6641732900666620295L;
	private Employee employee;
	private String type;
	private Room room;
	private long ID;
	
	public Task() {}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "Task [employee=" + employee + ", type=" + type + ", room="
				+ room + "]";
	}

	public long getID() {
		return ID;
	}

	public void setID(long ID) {
		this.ID = ID;
	}
	
}
