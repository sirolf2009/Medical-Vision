package org.medicalvision.server.core.model;

public class SubTask {

	private String description;
	private boolean completed;
	
	public SubTask() {}
	
	public SubTask(String description) {
		setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

}
