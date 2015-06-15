package org.medicalvision.service;
import static org.medicalvision.service.Util.file;
import static org.medicalvision.service.Util.fileVelocity;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

import java.util.HashMap;
import java.util.Map;

import org.medicalvision.neural.MVNetworkManager;
import org.medicalvision.server.core.model.Employee;
import org.medicalvision.server.core.model.Patient;
import org.medicalvision.service.paths.MVRoute;
import org.medicalvision.service.paths.RouteEmployee;
import org.medicalvision.service.paths.RoutePatient;
import org.medicalvision.service.paths.RouteRoom;
import org.medicalvision.service.paths.RouteSensor;
import org.medicalvision.service.paths.RouteTask;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;

import com.esotericsoftware.kryonet.Connection;
import com.google.gson.Gson;


public class MVService {

	public static final Map<Employee, Connection> onlineEmployees = new HashMap<Employee, Connection>();
	public static final DatabaseManager databaseManager = new DatabaseManager();
	public static final Gson gson = new Gson();

	public static RouteEmployee employee;
	public static RoutePatient patient;
	public static RouteTask task;
	public static RouteSensor sensor;
	public static RouteRoom room;

	public static void main(String[] args) {
		employee = new RouteEmployee();
		patient = new RoutePatient();
		task = new RouteTask();
		sensor = new RouteSensor();
		room = new RouteRoom();
		
		staticFileLocation("/assets");

		addRoute("/employee", "/add/:firstname/:lastname", employee);
		addRoute("/patient", "/add/:firstname/:lastname/:employeeID", patient);
		addRoute("/task", "/add/:type/:employeeID/:patientID", task);
		addRoute("/sensor", "/add/:sensorID/:roomID/:value", sensor);
		addRoute("/room", "/add/:roomID/:patientID", room);

		//		get("/employee/add/:firstname/:lastname", (req, res) -> employee.add().handle(req, res));
		//		get("/task/add/:type/:employeeID/:patientID", (req, res) -> employee.remove.handle(req, res));
		//		get("/sensor/:sensorID/:roomID/:value", (req, res) -> sensor.add().handle(req, res));
		get("/patient/assign/:patientID/:employeeID", RoutePatient.assign);
		get("/db/rawQuery/:password/:query", (req, res) -> sendCommand(req.params(":password"), req.params(":query")));

		exception(Exception.class, new ExceptionHandler() {
			@Override
			public void handle(Exception exception, Request request, Response response) {
				exception.printStackTrace();
				response.body("500 Internal Server Error\n"+exception);
			}
		});

		get("/", (req, res) -> file("index.html"));
		get("/pattern", (req, res) -> fileVelocity("pattern.html", "patient", new Patient("Floris", "Thijssen"), "networks", MVNetworkManager.getInstance().getNetworks().get(0)), new VelocityTemplateEngine());

		new KryoServer();
	}

	private static Object sendCommand(String pass, String query) {
		if(pass.equals("pispaus666")) {
			return databaseManager.getBeanTx().getService().execute(query).resultAsString();
		}
		return "403 unauthenticated";
	}

	public static void addRoute(String root, String getUrl, MVRoute<?> route) {
		get(root+getUrl, (req, res) -> route.add().handle(req, res));
		get(root+"/get/:id", (req, res) -> route.get.handle(req, res), gson::toJson);
		get(root+"/all", (req, res) -> route.all.handle(req, res), gson::toJson);
		get(root+"/remove", (req, res) -> route.all.handle(req, res), gson::toJson);
	}

}
