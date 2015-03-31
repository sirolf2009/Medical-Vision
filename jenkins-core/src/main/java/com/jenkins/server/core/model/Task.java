package com.jenkins.server.core.model;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class Task {

	private String name;
	private Employee employee;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
}
