package org.medicalvision.server.core.model;

public class Task {

	private Employee employee;
	private TaskType type;
	
	public Task() {}
	
	public Task(Employee employee, TaskType type) {
		setEmployee(employee);
		setType(type);
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public TaskType getType() {
		return type;
	}
	public void setType(TaskType type) {
		this.type = type;
	}
	
}
