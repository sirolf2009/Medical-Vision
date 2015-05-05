package org.medicalvision.server.core;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

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

	public static void main(String[] args) throws IOException, URISyntaxException {
		final HttpServer server = startServer();
		System.out.println(String.format("Jersey app started with WADL available at "
				+ "%sapplication.wadl\nHit enter to stop it...", ""));
		if(System.in.read() == 10) {
			server.shutdownNow();
			//restartApplication();
		}
		server.shutdownNow();
	}

	public static void restartApplication() throws URISyntaxException, IOException {
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		final File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());

		/* is it a jar file? */
		if(!currentJar.getName().endsWith(".jar"))
			return;

		/* Build command: java -jar application.jar */
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
		System.exit(0);
	}
}

