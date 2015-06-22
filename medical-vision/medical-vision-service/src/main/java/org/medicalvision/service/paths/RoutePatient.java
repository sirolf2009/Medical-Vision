package org.medicalvision.service.paths;

import org.medicalvision.server.core.model.Patient;
import org.medicalvision.service.DatabaseManager.Manager;
import org.medicalvision.service.MVService;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import spark.Request;
import spark.Response;
import spark.Route;
import static org.medicalvision.service.Util.paramAsLong;

public class RoutePatient extends MVRoute<Patient> {

	public static final Route assign = new Route() {
		@Override
		public Object handle(Request req, Response res) throws Exception {
			GraphDatabaseService service = MVService.databaseManager.getBeanTx().getService();
			try(Transaction tx = service.beginTx()) {
				Relationship rel = service.getNodeById(paramAsLong(req, ":employeeID")).createRelationshipTo(service.getNodeById(paramAsLong(req, ":patientID")), DynamicRelationshipType.withName("TAKES_CARE_OF"));
				tx.success();
				return rel.getId();
			}
		}
	};
	
	@Override
	public Route add() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				Patient patient = new Patient();
				patient.setFirstName(request.params(":firstname"));
				patient.setLastName(request.params(":lastname"));
				patient.setCareTaker(MVService.databaseManager.getEmployeeManager().pull(paramAsLong(request, ":employeeID")));
				return MVService.databaseManager.getPatientManager().push(patient);
			}
		};
	}

	@Override
	public Manager<?> getManager() {
		return MVService.databaseManager.getPatientManager();
	}

}
