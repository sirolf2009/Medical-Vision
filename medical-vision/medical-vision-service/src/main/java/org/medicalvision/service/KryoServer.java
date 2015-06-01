package org.medicalvision.service;

import java.io.IOException;

import org.medicalvision.server.core.Configuration;
import org.medicalvision.server.core.Constants;
import org.medicalvision.server.core.kryo.PacketConnect;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KryoServer {

	private Server server;

	public KryoServer() {
		Configuration config = Configuration.getInstance();
		config.loadKryoConfig();
		setServer(new Server());
		try {
			getServer().bind(config.KRYO_PORT, config.KTYO_TIMEOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Class<?> clazz : Constants.KRYO_PACKETS) {
			getServer().getKryo().register(clazz);
		}
		getServer().addListener(new Listener() {
			@Override
			public void received (Connection connection, Object object) {
				if (object instanceof PacketConnect) {
					PacketConnect connect = (PacketConnect) object;
					MVService.onlineEmployees.put(connect.getEmployee(), connection);
				}
			}
		});
		getServer().start();
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

}
