package org.medicalvision.service.paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class ServiceDefault {

	@GET
	public String get() {
		return "404 :3";
	}
}
