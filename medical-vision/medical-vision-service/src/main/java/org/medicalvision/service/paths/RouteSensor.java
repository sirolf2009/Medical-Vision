package org.medicalvision.service.paths;

import static org.medicalvision.service.Util.paramAsDouble;
import static org.medicalvision.service.Util.paramAsInt;

import java.util.List;

import org.medical.vision.neural.MVNetworkManager;
import org.medicalvision.server.core.model.SensorData;
import org.medicalvision.server.core.model.Task;
import org.medicalvision.service.DatabaseManager.Manager;
import org.medicalvision.service.MVService;

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
				data.setTimestamp(System.currentTimeMillis());
				MVService.databaseManager.getSensorManager().push(data);
				@SuppressWarnings("unused")
				List<Task> tasks = MVNetworkManager.getInstance().process(MVService.databaseManager.getSensorManager().all());
				//TODO assign tasks to employees
				//TODO assign tasks to patients
				//TODO notify employees
				return data;
			}
		};
	}
	
	@Override
	public Manager<?> getManager() {
		return MVService.databaseManager.getSensorManager();
	}

}
