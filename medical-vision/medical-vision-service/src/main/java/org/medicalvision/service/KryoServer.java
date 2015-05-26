package org.medicalvision.service;

import org.medicalvision.server.core.Configuration;
import org.medicalvision.server.core.Constants;
import org.medicalvision.server.core.kryo.PacketIP;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KryoServer {

	private Server server;

	public KryoServer() {
		Configuration config = Configuration.getInstance();
		config.loadKryoConfig();
		setServer(new Server(config.KRYO_PORT_START, config.KRYO_PORT_END));
		for(Class<?> clazz : Constants.KRYO_PACKETS) {
			getServer().getKryo().register(clazz);
		}
		getServer().addListener(new Listener() {
			@Override
			public void received (Connection connection, Object object) {
				if (object instanceof PacketIP) {
					PacketIP ip = (PacketIP) object;
					MVService.onlineEmployees.put(ip.getEmployee(), ip.getMobileIP());
				}
			}
		});
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

}
