package org.medicalvision.service;
import static spark.Spark.exception;
import static spark.Spark.get;

import java.util.HashMap;
import java.util.Map;

import org.medicalvision.server.core.model.Employee;
import org.medicalvision.service.paths.MVRoute;
import org.medicalvision.service.paths.RouteEmployee;
import org.medicalvision.service.paths.RoutePatient;
import org.medicalvision.service.paths.RouteRoom;
import org.medicalvision.service.paths.RouteSensor;
import org.medicalvision.service.paths.RouteTask;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

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

		addRoute("/employee", "/add/:firstname/:lastname", employee);
		addRoute("/patient", "/add/:firstname/:lastname/:employeeID", patient);
		addRoute("/task", "/add/:type/:employeeID/:patientID", task);
		addRoute("/sensor", "/add/:sensorID/:roomID/:value", sensor);
		addRoute("/room", "/add/:roomID/:patientID", room);
		
//		get("/employee/add/:firstname/:lastname", (req, res) -> employee.add().handle(req, res));
//		get("/task/add/:type/:employeeID/:patientID", (req, res) -> employee.remove.handle(req, res));
//		get("/sensor/:sensorID/:roomID/:value", (req, res) -> sensor.add().handle(req, res));
		get("/patient/assign/:patientID/:employeeID", RoutePatient.assign);
		
		exception(Exception.class, new ExceptionHandler() {
			@Override
			public void handle(Exception exception, Request request, Response response) {
				response.body("500 Internal Server Error\n"+exception);
			}
		});
		
		new KryoServer();
	}

	public static void addRoute(String root, String getUrl, MVRoute<?> route) {
		get(root+getUrl, (req, res) -> route.add().handle(req, res));
		get(root+"/get/:id", (req, res) -> route.get.handle(req, res), gson::toJson);
		get(root+"/all", (req, res) -> route.all.handle(req, res), gson::toJson);
		get(root+"/remove", (req, res) -> route.all.handle(req, res), gson::toJson);
	}

}
