package org.medicalvision.service.paths;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.medicalvision.server.core.model.Employee;
import org.medicalvision.service.Main;

@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceEmployee {

	@GET
	@Path("/")
    public String defaultGet() {
        return "404 :3";
    }

	@GET
	@Path("/add/{firstName}/{lastName}")
	public long addEmployee(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
		Employee employee = new Employee();
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		return Main.databaseManager.getEmployeeManager().push(employee);
	}

	@GET
	@Path("/get/{ID}")
	public Employee addEmployee(@PathParam("ID") long id) {
		return Main.databaseManager.getEmployeeManager().pull(id);
	}

	@GET
	@Path("/all")
	public List<Employee> all() {
		return Main.databaseManager.getEmployeeManager().all();
	}

}
