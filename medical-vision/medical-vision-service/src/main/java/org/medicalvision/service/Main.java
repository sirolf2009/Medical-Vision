package org.medicalvision.service;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.medicalvision.server.core.Configuration;
import org.medicalvision.server.core.model.Employee;

public class Main {
	
	public static DatabaseManager databaseManager;
	
	public static final Map<Employee, String> onlineEmployees = new HashMap<Employee, String>();
	
	public static HttpServer startServer() {
    	Configuration config = Configuration.getInstance();
    	config.loadServerConfig();
        final ResourceConfig rc = new ResourceConfig().packages("org.medicalvision.service");
        rc.register(JacksonFeature.class);
        HttpServer grizzly = GrizzlyHttpServerFactory.createHttpServer(URI.create(config.SERVER_BASE_URI), rc);
        grizzly.getServerConfiguration().setAllowPayloadForUndefinedHttpMethods(true);
        return grizzly;
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
    	Configuration config = Configuration.getInstance();
    	databaseManager = new DatabaseManager();
    	
    	
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", config.SERVER_BASE_URI));
        System.in.read();
        server.shutdownNow();
    }
}

