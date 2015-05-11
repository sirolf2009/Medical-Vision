package org.medicalvision.server.core.model;

public class Employee extends Person {

	@Override
	public String toString() {
		return "Employee [getFirstName()=" + getFirstName()
				+ ", getLastName()=" + getLastName() + "]";
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Employee) {
			Employee employee = (Employee) other;
			return getFirstName().equals(employee.getFirstName()) &&
					getLastName().equals(employee.getLastName());
		}
		return false;
	}

}
