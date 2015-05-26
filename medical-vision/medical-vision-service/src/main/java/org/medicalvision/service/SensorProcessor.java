package org.medicalvision.service;

import java.io.IOException;
import java.net.URL;

import org.medicalvision.server.core.model.SensorData;

public class SensorProcessor {
	
	public static void notify(SensorData sensorData) {
		if(sensorData.getValue() >= 1) {
			try {
				new URL("172.0.0.1/task/add/0/0/0").openConnection().getContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
