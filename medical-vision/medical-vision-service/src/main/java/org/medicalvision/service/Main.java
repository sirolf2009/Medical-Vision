package org.medicalvision.service;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.medicalvision.server.core.Configuration;

import java.io.IOException;
import java.net.URI;

public class Main {
	
	public static HttpServer startServer() {
    	Configuration config = Configuration.getInstance();
    	config.loadServerConfig();
        final ResourceConfig rc = new ResourceConfig().packages("org.medicalvision.service");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(config.SERVER_BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
    	Configuration config = Configuration.getInstance();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", config.SERVER_BASE_URI));
        System.in.read();
        server.shutdownNow();
    }
}

