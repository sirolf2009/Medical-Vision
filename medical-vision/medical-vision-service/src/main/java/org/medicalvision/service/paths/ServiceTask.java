package org.medicalvision.service.paths;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.medicalvision.server.core.model.Employee;
import org.medicalvision.server.core.model.Task;
import org.medicalvision.service.Main;

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceTask {

	@GET
	@Path("/")
	public String defaultGet() {
		return "404 :3";
	}

	@GET
	@Path("/add/{employeeID}/{patientID}/{taskType}")
	public long add(@PathParam("employeeID") long employeeID, @PathParam("patientID") long patientID, @PathParam("taskType") String taskType) {
		Employee employee = Main.databaseManager.getEmployeeManager().pull(employeeID);
		Task task = new Task();
		task.setEmployee(employee);
		task.setPatient(Main.databaseManager.getPatientManager().pull(patientID));
		return Main.databaseManager.getTaskManager().push(task); //TODO make thrift call
	}

	@GET
	@Path("/get/{taskID}")
	public Task get(@PathParam("taskID") long taskID) {
		return Main.databaseManager.getTaskManager().pull(taskID);
	}

	@GET
	@Path("/remove/{taskID}")
	public boolean remove(@PathParam("taskID") long taskID) {
		try {
			Main.databaseManager.getTaskManager().remove(taskID);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	@GET
	@Path("/all")
	public List<Task> all() {
		return Main.databaseManager.getTaskManager().all();
	}

}
