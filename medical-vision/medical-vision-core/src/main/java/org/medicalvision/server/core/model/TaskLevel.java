package org.medicalvision.server.core.model;

public enum TaskLevel {
	
	LOW(4),
	MEDIUM(3),
	HIGH(2),
	CRITICAL(0),
	UNKNOWN(1);
	
	private int priority;
	
	private TaskLevel(int priority) {
		setPriority(priority);
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
