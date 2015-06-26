package org.medicalvision.server.core.model;

import java.io.Serializable;

public class Patient implements Serializable {

	private static final long serialVersionUID = 5079700701359275537L;
	private String firstName;
	private String lastName;
	private Employee careTaker;
	
	public Patient(String firstName, String lastName, Employee careTaker) {
		this(firstName, lastName);
		setCareTaker(careTaker);
	}
	
	public Patient(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	public Patient() {
	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((careTaker == null) ? 0 : careTaker.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
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
		Patient other = (Patient) obj;
		if (careTaker == null) {
			if (other.careTaker != null)
				return false;
		} else if (!careTaker.equals(other.careTaker))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}
	
}
