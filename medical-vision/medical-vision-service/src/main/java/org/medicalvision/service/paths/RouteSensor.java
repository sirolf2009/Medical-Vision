package org.medicalvision.service.paths;

import static org.medicalvision.service.Util.paramAsDouble;
import static org.medicalvision.service.Util.paramAsInt;


import java.util.List;


import org.medicalvision.neural.MVNetworkManager;
import org.medicalvision.server.core.model.Employee;
import org.medicalvision.server.core.model.SensorData;
import org.medicalvision.server.core.model.Task;
import org.medicalvision.service.DatabaseManager.Manager;
import org.medicalvision.service.MVService;


import scala.util.Random;
import spark.Request;
import spark.Response;
import spark.Route;


import com.esotericsoftware.kryonet.Connection;


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
				List<Task> tasks = MVNetworkManager.getInstance().process(MVService.databaseManager.getSensorManager().all());
				tasks.forEach(task -> {
					task.setRoom(MVService.databaseManager.getRoomFromID(data.getRoomID()));
					task.setEmployee(task.getRoom().getPatient().getCareTaker());
					Employee assignee = null;
					if(MVService.onlineEmployees.containsKey(task.getEmployee())) {
						assignee = task.getEmployee();
					} else if(MVService.onlineEmployees.size() > 1) { // Pick random assigne
						assignee = MVService.onlineEmployees.values().toArray(new Employee[MVService.onlineEmployees.size()])[new Random().nextInt(MVService.onlineEmployees.size())];
					} else {
						System.err.println("No employees online for Task "+task);
					}
					if (assignee != null) {
						Connection conn = MVService.onlineEmployees.get(task.getEmployee());
						conn.sendTCP(task);
					}
						
				});
				return data;
			}
		};
	}
	
	@Override
	public Manager<?> getManager() {
		return MVService.databaseManager.getSensorManager();
	}

}
