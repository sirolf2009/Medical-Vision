package org.medicalvision.server.core.model;

import static org.medicalvision.server.core.model.TaskLevel.CRITICAL;
import static org.medicalvision.server.core.model.TaskLevel.HIGH;
import static org.medicalvision.server.core.model.TaskLevel.LOW;
import static org.medicalvision.server.core.model.TaskLevel.MEDIUM;
import static org.medicalvision.server.core.model.TaskLevel.UNKNOWN;

import java.util.HashMap;
import java.util.Map;

public enum TaskType {

	OBSERVE("Observeer", "Observeer zorgvrager", LOW, "Observeer"),
	CLEAN_BED("Verschoon_Bed", "Verschoon het bed van de zorgvrager", LOW, "Haal nieuwe beddengoed,Vervang beddengoed,Was oude beddengoed"),
	CLEAN_ROOM("Verschoon_Kamer", "Maak de kamer van de zorgvrager schoon", LOW, "Stofzuigen,Dweilen"),
	
	AID_WAKE_UP("Wakker maken", "Maak zorgvrager wakker", MEDIUM, "Maak zorgvrager wakker"),
	AID_DOUCHE("Douchen", "Help zorgvrager met douchen", MEDIUM, "Kleed zorgvrager uit,Was zorgvrager,Kleed zorgvrager aan"),
	AID_DRESS("Aankleden", "Help zorgvrager met aankleden", MEDIUM, "Kleed de zorgvrager aan"),
	AID_WC("WC", "Help zorgvrager met de wc", MEDIUM, "Help zorgvrager met de wc"),
	AID_PUT_IN_BED("Instoppen", "Begeleid zorgvrager naar bed", MEDIUM, "Begeleid zorgvrager naar bed"),
	
	SERVE_BREAKFAST("Ontbijt", "Serveer ontbijt", HIGH, "Haal eten op,Serveer eten,Haal servies op"),
	SERVE_MEDICINE("Medicijnen", "Dien medicijnen in", HIGH, "Haal medicijnen op,Dien medicijnen in"),
	SERVE_COFFEE("Koffie", "Serveer koffie", HIGH, "Maak koffie,Serveer koffie"),
	SERVE_MEAL("Maaltijd", "Serveer maaltijd", HIGH, "Kook een maaltijd,Serveer de maaltijd"),
	
	EMERGENCY_UNKNOWN("Onbekend", "Onbekend noodgeval", CRITICAL, "Kijk of alles goed gaat met de zorgvrager"),
	EMERGENCY_PISSED_IN_BED("Bedplasser", "Ruim incontinentie materiaal op", CRITICAL, "Ruim incontinentiemateriaal op"),
	EMERGENCY_PERSON_WANDERING("Dwalende_zorgvrager", "Begeleid zorgvrager", CRITICAL, "Begeleid de zorgvrager terug naar zijn kamer"),
	EMERGENCY_HIGH_HARTRATE("Hoog_hartslag", "Verhoogd hartslag", CRITICAL, "Verhelp de hoge hartslag"),
	EMERGENCY_LOW_HARTRATE("Laag_hartslag", "Verlaagd hartslag", CRITICAL, "Verhelp de lage hartslag"),
	EMERGENCY_FALLEN_CLIENT("Vallende_zorgvrager", "Een zorgvrager is gevallen", CRITICAL, "Help de zorgvrager overeind"),
	
	OTHER("Onbekend", "Onbekend", UNKNOWN, "Kijk of alles goed gaat met de zorgvrager");

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
