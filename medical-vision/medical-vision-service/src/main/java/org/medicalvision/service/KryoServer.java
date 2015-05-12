package org.medicalvision.service;

import org.medicalvision.server.core.Configuration;

import com.esotericsoftware.kryonet.Server;

public class KryoServer {

	private Server server;
	
	public KryoServer() {
		Configuration config;
		server = new Server(0, 0);
	}

}
