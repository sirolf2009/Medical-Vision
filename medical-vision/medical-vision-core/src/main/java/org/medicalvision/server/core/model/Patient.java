package org.medicalvision.server.core.model;

public class Patient {

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
	
}
