package org.medicalvision.service.paths;

import static org.medicalvision.service.MVService.databaseManager;
import static org.medicalvision.service.Util.paramAsLong;

import org.medicalvision.server.core.model.Task;
import org.medicalvision.service.DatabaseManager.Manager;
import org.medicalvision.service.MVService;
import org.medicalvision.service.thrift.Client;

import spark.Request;
import spark.Response;
import spark.Route;

public class RouteTask extends MVRoute<Task> {

	@Override
	public Route add() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				Task task = new Task();
				task.setType(request.params(":firstname"));
				task.setEmployee(databaseManager.getEmployeeManager().pull(paramAsLong(request, ":employeeID")));
				task.setPatient(databaseManager.getPatientManager().pull(paramAsLong(request, ":patientID")));
				String ip = MVService.onlineEmployees.get(databaseManager.getEmployeeManager().pull(paramAsLong(request, ":employeeID")));
				new Client(ip).notifyOfTask(task);
				return databaseManager.getTaskManager().push(task);
			}
		};
	}
	
	@Override
	public Manager<?> getManager() {
		return MVService.databaseManager.getEmployeeManager();
	}

}
