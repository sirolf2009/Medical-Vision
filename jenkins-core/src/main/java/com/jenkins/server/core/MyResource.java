package com.jenkins.server.core;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.jenkins.server.core.model.Employee;
import com.jenkins.server.core.model.Task;

@Path("task")
public class MyResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Object getIt() {
    	Employee employee = new Employee();
    	employee.setName("Floris Thijssen");
    	Task task = new Task();
    	task.setEmployee(employee);
    	task.setName("Go do work");
        return task.toString();
    }
}
