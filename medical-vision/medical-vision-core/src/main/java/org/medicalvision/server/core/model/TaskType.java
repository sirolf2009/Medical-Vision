package org.medicalvision.server.core.model;

import static org.medicalvision.server.core.model.TaskLevel.CRITICAL;
import static org.medicalvision.server.core.model.TaskLevel.HIGH;
import static org.medicalvision.server.core.model.TaskLevel.LOW;
import static org.medicalvision.server.core.model.TaskLevel.MEDIUM;
import static org.medicalvision.server.core.model.TaskLevel.UNKNOWN;

import java.util.HashMap;
import java.util.Map;

public enum TaskType {

	OBSERVE("Observeer", "Observeer zorgvrager", LOW, "Fuck,Off"),
	CLEAN_BED("Verschoon_Bed", "Verschoon het bed van de zorgvrager", LOW, "Fuck,Off"),
	CLEAN_ROOM("Verschoon_Kamer", "Maak de kamer van de zorgvrager schoon", LOW, "Fuck,Off"),
	
	AID_WAKE_UP("Wakker maken", "Maak zorgvrager wakker", MEDIUM, "Fuck,Off"),
	AID_DOUCHE("Douchen", "Help zorgvrager met douchen", MEDIUM, "Fuck,Off"),
	AID_DRESS("Aankleden", "Help zorgvrager met aankleden", MEDIUM, "Fuck,Off"),
	AID_WC("WC", "Help zorgvrager met de wc", MEDIUM, "Fuck,Off"),
	AID_PUT_IN_BED("Instoppen", "Begeleid zorgvrager naar bed", MEDIUM, "Fuck,Off"),
	
	SERVE_BREAKFAST("Ontbijt", "Serveer ontbijt", HIGH, "Haal eten op,Serveer eten,Haal servies op"),
	SERVE_MEDICINE("Medicijnen", "Dien medicijnen in", HIGH, "Fuck,Off"),
	SERVE_COFFEE("Koffie", "Serveer koffie", HIGH, "Fuck,Off"),
	SERVE_MEAL("Maaltijd", "Serveer maaltijd", HIGH, "Fuck,Off"),
	
	EMERGENCY_UNKNOWN("Onbekend", "Onbekend noodgeval", CRITICAL, "Fuck,Off"),
	EMERGENCY_PISSED_IN_BED("Bedplasser", "Ruim incontinentie materiaal op", CRITICAL, "Fuck,Off"),
	EMERGENCY_PERSON_WANDERING("Dwalende_zorgvrager", "Begeleid zorgvrager", CRITICAL, "Fuck,Off"),
	EMERGENCY_HIGH_HARTRATE("Hoog_hartslag", "Verhoogd hartslag", CRITICAL, "Fuck,Off"),
	EMERGENCY_LOW_HARTRATE("Laag_hartslag", "Verlaagd hartslag", CRITICAL, "Fuck,Off"),
	EMERGENCY_FALLEN_CLIENT("Vallende_zorgvrager", "Een zorgvrager is gevallen", CRITICAL, "Fuck,Off"),
	
	OTHER("Onbekend", "Onbekend", UNKNOWN, "Fuck,Off");

	private String description;
	private String name;
	private TaskLevel level;
	private SubTask[] subTasks;
	
	private static final Map<String, TaskType> tasks = new HashMap<String, TaskType>();
	
	private TaskType(final String name, String description, TaskLevel level, SubTask... subTasks) {
		setName(name);
		setDescription(description);
		setLevel(level);
		setSubTasks(subTasks);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				tasks.put(name, TaskType.this);
			}
		}).start();
	}
	
	private TaskType(String name, String description, TaskLevel level, String subTasks) {
		this(name, description, level, parseSubTasks(subTasks));
	}
	
	public static SubTask[] parseSubTasks(String subString) {
		String[] descriptions = subString.split(",");
		SubTask[] subTasks = new SubTask[descriptions.length];
		for (int i = 0; i < descriptions.length; i++) {
			subTasks[i] = new SubTask(descriptions[i]);
		}
		return subTasks;
	}
	
	public static TaskType fromName(String name) {
		return tasks.get(name);
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
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
