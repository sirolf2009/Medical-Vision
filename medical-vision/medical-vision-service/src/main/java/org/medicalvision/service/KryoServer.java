package org.medicalvision.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.medicalvision.server.core.kryo.PacketConnect;
import org.medicalvision.server.core.model.EmergencyResult;
import org.medicalvision.server.core.model.Employee;
import org.medicalvision.server.core.model.Patient;
import org.medicalvision.server.core.model.Room;
import org.medicalvision.server.core.model.Task;
import org.medicalvision.server.core.model.TaskType;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
public class KryoServer {

	private Server server;

	public KryoServer() {
		Server server = new Server();
		try {
			server.bind(8100, 8101);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		server.start();
		server.getKryo().register(PacketConnect.class);
		server.getKryo().register(Employee.class);
		server.getKryo().register(Room.class);
		server.getKryo().register(Patient.class);
		server.getKryo().register(Task.class);
		server.getKryo().register(TaskType.class);
		server.getKryo().register(EmergencyResult.class);
		server.addListener(new Listener() {
			
			@Override
			public void received (Connection connection, Object object) {
				if(!(object instanceof KeepAlive)) {
					System.out.println(object);
				}
				if(object instanceof PacketConnect) {
					PacketConnect connect = (PacketConnect) object;
					MVService.onlineEmployees.put(connect.getEmployee(), connection);
					System.out.println(MVService.onlineEmployees);
					
					Task task = new Task();
					task.setEmployee(connect.getEmployee());
					Room room1 = MVService.databaseManager.getRoomFromID(3);
					task.setRoom(room1);
					task.setType(TaskType.AID_DOUCHE.toString());
					connection.sendTCP(task);
					task.setType(TaskType.AID_DRESS.toString());
					connection.sendTCP(task);
					task.setType(TaskType.SERVE_COFFEE.toString());
					connection.sendTCP(task);
					task.setType(TaskType.SERVE_BREAKFAST.toString());
					connection.sendTCP(task);
					
					Room room2 = MVService.databaseManager.getRoomFromID(2);
					task.setRoom(room2);
					task.setType(TaskType.AID_DOUCHE.toString());
					connection.sendTCP(task);
					task.setType(TaskType.AID_DRESS.toString());
					connection.sendTCP(task);
					task.setType(TaskType.SERVE_COFFEE.toString());
					connection.sendTCP(task);
					task.setType(TaskType.SERVE_BREAKFAST.toString());
					connection.sendTCP(task);
				}
				if(object instanceof EmergencyResult) {
					//TODO
				}
			}
			
			@Override
			public void disconnected(Connection arg0) {
				List<Employee> dirty = new ArrayList<Employee>();
				for(Entry<Employee, Connection> entry : MVService.onlineEmployees.entrySet()) {
					if(entry.getValue().equals(arg0)) {
						dirty.add(entry.getKey());
					}
				}
				for(Employee employee : dirty) {
					MVService.onlineEmployees.remove(employee);
					System.out.println(MVService.onlineEmployees);
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
