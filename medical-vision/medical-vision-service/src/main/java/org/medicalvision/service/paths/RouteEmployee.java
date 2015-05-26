package org.medicalvision.service.paths;

import org.medicalvision.server.core.model.Employee;
import org.medicalvision.service.MVService;
import org.medicalvision.service.DatabaseManager.Manager;
import spark.Request;
import spark.Response;
import spark.Route;

public class RouteEmployee extends MVRoute<Employee> {

	@Override
	public Route add() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				Employee employee = new Employee();
				employee.setFirstName(request.params(":firstname"));
				employee.setLastName(request.params(":lastname"));
				return MVService.databaseManager.getEmployeeManager().push(employee);
			}
		};
	}
	
	@Override
	public Manager<?> getManager() {
		return MVService.databaseManager.getEmployeeManager();
	}

}
