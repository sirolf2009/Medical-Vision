package org.medicalvision.server.core.kryo;

import org.medicalvision.server.core.model.Employee;

public class PacketConnect {

	private Employee employee;
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
