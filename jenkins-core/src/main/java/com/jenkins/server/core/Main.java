package com.jenkins.server.core;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {

    public static HttpServer startServer() {
    	Configuration config = Configuration.getInstance();
    	config.loadServerConfig();
        final ResourceConfig rc = new ResourceConfig().packages("com.jenkins.server.core");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(config.SERVER_BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", ""));
        System.out.println("dfhdth");
        System.in.read();
        server.shutdownNow();
    }
}

