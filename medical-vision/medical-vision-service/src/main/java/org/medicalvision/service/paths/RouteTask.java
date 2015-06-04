package org.medicalvision.service.paths;

import static org.medicalvision.service.MVService.databaseManager;
import static org.medicalvision.service.Util.paramAsLong;

import org.medicalvision.server.core.model.Task;
import org.medicalvision.service.DatabaseManager.Manager;
import org.medicalvision.service.MVService;

import spark.Request;
import spark.Response;
import spark.Route;

import com.esotericsoftware.kryonet.Connection;

public class RouteTask extends MVRoute<Task> {

	@Override
	public Route add() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				Task task = new Task();
				task.setType(request.params(":firstname"));
				task.setEmployee(databaseManager.getEmployeeManager().pull(paramAsLong(request, ":employeeID")));
				task.setRoom(databaseManager.getRoomManager().pull(paramAsLong(request, ":roomID")));
				Connection conn = MVService.onlineEmployees.get(databaseManager.getEmployeeManager().pull(paramAsLong(request, ":employeeID")));
				conn.sendTCP(task);
				return databaseManager.getTaskManager().push(task);
			}
		};
	}
	
	@Override
	public Manager<?> getManager() {
		return MVService.databaseManager.getEmployeeManager();
	}

}
