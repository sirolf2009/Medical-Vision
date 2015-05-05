package org.medicalvision.server.core.model;

import static org.medicalvision.server.core.model.TaskLevel.*;

public enum TaskType {

	OBSERVE("Observeer zorgvrager", LOW, "Fuck,Off"),
	CLEAN_BED("Verschoon het bed van de zorgvrager", LOW, "Fuck,Off"),
	CLEAN_ROOM("Maak de kamer van de zorgvrager schoon", LOW, "Fuck,Off"),
	
	AID_WAKE_UP("Maak zorgvrager wakker", MEDIUM, "Fuck,Off"),
	AID_DOUCHE("Help zorgvrager met douchen", MEDIUM, "Fuck,Off"),
	AID_DRESS("Help zorgvrager met aankleden", MEDIUM, "Fuck,Off"),
	AID_WC("Help zorgvrager met de wc", MEDIUM, "Fuck,Off"),
	AID_PUT_IN_BED("Begeleid zorgvrager naar bed", MEDIUM, "Fuck,Off"),
	
	SERVE_BREAKFAST("Serveer ontbijt", HIGH, "Fuck,Off"),
	SERVE_MEDICINE("Dien medicijnen in", HIGH, "Fuck,Off"),
	SERVE_COFFEE("Serveer koffie", HIGH, "Fuck,Off"),
	SERVE_MEAL("Serveer maaltijd", HIGH, "Fuck,Off"),
	
	EMERGENCY_UNKNOWN("Onbekend noodgeval", CRITICAL, "Fuck,Off"),
	EMERGENCY_PISSED_IN_BED("Bedplasser", CRITICAL, "Fuck,Off"),
	EMERGENCY_PERSON_WANDERING("Dwalende zorgvrager", CRITICAL, "Fuck,Off"),
	EMERGENCY_HIGH_HARTRATE("Verhoogd hartslag", CRITICAL, "Fuck,Off"),
	EMERGENCY_LOW_HARTRATE("Verlaagd hartslag", CRITICAL, "Fuck,Off"),
	
	OTHER("Onbekend", UNKNOWN, "Fuck,Off");
	
	private String description;
	private TaskLevel level;
	private SubTask[] subTasks;
	
	private TaskType(String description, TaskLevel level, SubTask... subTasks) {
		setDescription(description);
		setLevel(level);
		setSubTasks(subTasks);
	}
	
	private TaskType(String description, TaskLevel level, String subTasks) {
		setDescription(description);
		setLevel(level);
		setSubTasks(ParseSubTasks(subTasks));
	}
	
	public static SubTask[] ParseSubTasks(String subString) {
		String[] descriptions = subString.split(",");
		SubTask[] subTasks = new SubTask[descriptions.length];
		for (int i = 0; i < descriptions.length; i++) {
			subTasks[i] = new SubTask(descriptions[i]);
		}
		return subTasks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SubTask[] getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(SubTask[] subTasks) {
		this.subTasks = subTasks;
	}

	public TaskLevel getLevel() {
		return level;
	}

	public void setLevel(TaskLevel level) {
		this.level = level;
	}
	
	@Override
	public String toString() {
		return "kanker";
	}
	
}
