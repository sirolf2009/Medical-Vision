package org.medicalvision.service.paths;

import org.medicalvision.server.core.model.Patient;
import org.medicalvision.service.DatabaseManager.Manager;
import org.medicalvision.service.MVService;

import spark.Request;
import spark.Response;
import spark.Route;

public class RoutePatient extends MVRoute<Patient> {

	@Override
	public Route add() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				Patient patient = new Patient();
				patient.setFirstName(request.params(":firstname"));
				patient.setLastName(request.params(":lastname"));
				return MVService.databaseManager.getPatientManager().push(patient);
			}
		};
	}

	@Override
	public Manager<?> getManager() {
		return MVService.databaseManager.getPatientManager();
	}

}
