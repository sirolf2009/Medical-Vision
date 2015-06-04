package org.medicalvision.service.paths;

import org.medicalvision.server.core.model.Room;
import org.medicalvision.service.MVService;
import org.medicalvision.service.Util;
import org.medicalvision.service.DatabaseManager.Manager;

import spark.Request;
import spark.Response;
import spark.Route;

public class RouteRoom extends MVRoute<Room> {

	@Override
	public Route add() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				Room room = new Room();
				room.setPatient(MVService.databaseManager.getPatientManager().pull(Util.paramAsLong(request, ":patientID")));
				room.setRoomID(Util.paramAsInt(request, ":roomID"));
				return room;
			}
		};
	}

	@Override
	public Manager<?> getManager() {
		return MVService.databaseManager.getRoomManager();
	}

}
