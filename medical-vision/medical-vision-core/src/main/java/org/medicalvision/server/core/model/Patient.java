package org.medicalvision.server.core.model;

import java.io.Serializable;

public class Patient implements Serializable {

	private static final long serialVersionUID = 5079700701359275537L;
	private String firstName;
	private String lastName;
	private Employee careTaker;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Employee getCareTaker() {
		return careTaker;
	}
	public void setCareTaker(Employee careTaker) {
		this.careTaker = careTaker;
	}
	@Override
	public String toString() {
		return "Patient [firstName=" + firstName + ", lastName=" + lastName
				+ ", careTaker=" + careTaker + "]";
	}
	
}
