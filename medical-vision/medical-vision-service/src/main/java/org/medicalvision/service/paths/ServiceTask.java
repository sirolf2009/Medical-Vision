package org.medicalvision.service.paths;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.medicalvision.server.core.model.Employee;
import org.medicalvision.server.core.model.Task;
import org.medicalvision.server.core.model.TaskType;

@Path("/task")
public class ServiceTask {
	
	public static List<Task> tasks = new ArrayList<Task>();

	@GET
	@Path("/")
    public String defaultGet() {
        return "404 :3";
    }
	
	@GET
	@Path("all")
    public Task get() {
    	Employee employee = new Employee();
    	employee.setFirstName("Floris");
    	employee.setLastName("Thijssen");
    	Task task = new Task();
    	task.setEmployee(employee);
    	task.setType(TaskType.CLEAN_BED);
        return task;
    }
	
	@GET
	@Path("test")
    public Employee getTest() {
    	Employee employee = new Employee();
    	employee.setFirstName("Floris");
    	employee.setLastName("Thijssen");
        return employee;
    }
	
	@GET
	@Path("/add/{ID}")
    public Task add(@PathParam("ID") String ID) {
    	Employee employee = new Employee();
    	employee.setFirstName(ID);
    	employee.setLastName("Thijssen");
    	Task task = new Task();
        return task;
    }
	
	@GET
	@Path("/add/{ID}/{USER}")
    public Task add(@PathParam("ID") String ID, @PathParam("USER") String USER) {
    	Employee employee = new Employee();
    	employee.setFirstName(USER);
    	Task task = new Task();
        return task;
    }
	
}
