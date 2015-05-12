package org.medicalvision.server.core.kryo;

import org.medicalvision.server.core.model.Employee;

public class PacketIP {

	private String mobileIP;
	private Employee employee;
	
	public String getMobileIP() {
		return mobileIP;
	}
	public void setMobileIP(String mobileIP) {
		this.mobileIP = mobileIP;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
