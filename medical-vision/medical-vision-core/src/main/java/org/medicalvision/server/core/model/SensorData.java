package org.medicalvision.server.core.model;

import java.io.Serializable;

public class SensorData implements Serializable {

	private static final long serialVersionUID = 5905368468737076644L;
	private int sensorID;
	private int roomID;
	private double value;
	private long timestamp;
	
	public SensorData() {}
	
	public SensorData(SensorData data) {
		setSensorID(data.getSensorID());
		setRoomID(data.getRoomID());
		setValue(value);
		setTimestamp(timestamp);
	}

	public int getSensorID() {
		return sensorID;
	}

	public void setSensorID(int sensorID) {
		this.sensorID = sensorID;
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "SensorData [sensorID=" + sensorID + ", roomID=" + roomID
				+ ", value=" + value + "]";
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
