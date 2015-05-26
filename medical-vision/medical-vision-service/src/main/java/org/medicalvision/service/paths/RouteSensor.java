package org.medicalvision.service.paths;

import static org.medicalvision.service.Util.paramAsDouble;
import static org.medicalvision.service.Util.paramAsInt;

import org.medicalvision.server.core.model.SensorData;
import org.medicalvision.service.MVService;
import org.medicalvision.service.DatabaseManager.Manager;
import org.medicalvision.service.SensorProcessor;

import spark.Request;
import spark.Response;
import spark.Route;

public class RouteSensor extends MVRoute<SensorData> {

	@Override
	public Route add() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SensorData data = new SensorData();
				data.setSensorID(paramAsInt(request, ":sensorID"));
				data.setRoomID(paramAsInt(request, ":roomID"));
				data.setValue(paramAsDouble(request, ":value"));
				MVService.databaseManager.getSensorManager().push(data);
				SensorProcessor.notify(data);
				return data;
			}
		};
	}
	
	@Override
	public Manager<?> getManager() {
		return MVService.databaseManager.getSensorManager();
	}

}
