package org.medicalvision.service.paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	@Path("/add/{employeeID}/{taskType}")
    public long add(@PathParam("employeeID") long employeeID, @PathParam("taskType") String taskType) {
    	return Main.databaseManager.pushTask(new Task(employeeID, taskType));
    }
	
	@GET
	@Path("/get/{taskID}")
    public Task add(@PathParam("taskID") long taskID) {
    	return Main.databaseManager.pullTask(taskID);
    }
	
	@GET
	@Path("/remove/{taskID}")
    public String remove(@PathParam("taskID") long taskID) {
    	Main.databaseManager.removeTask(taskID);
    	return "removed "+taskID;
    }
	
}
