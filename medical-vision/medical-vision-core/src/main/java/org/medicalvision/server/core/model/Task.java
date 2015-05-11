package org.medicalvision.server.core.model;

public class Task {

	private long employee;
	private String type;
	
	public Task() {}
	
	public Task(long employee, String type) {
		setEmployee(employee);
		setType(type);
	}
	
	public long getEmployee() {
		return employee;
	}
	public void setEmployee(long employee) {
		this.employee = employee;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
