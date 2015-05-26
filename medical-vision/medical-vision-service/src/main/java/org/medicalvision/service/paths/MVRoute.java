package org.medicalvision.service.paths;

import org.medicalvision.service.DatabaseManager.Manager;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class MVRoute<E> {

	public Route all = new Route() {
		public Object handle(Request request, Response response) throws Exception {
			return getManager().all();
		}
	};

	public Route get = new Route() {
		@Override
		public Object handle(Request request, Response response) throws Exception {
			long id = Long.parseLong(request.params(":id"));
			return getManager().pull(id);
		}
	};

	public Route remove = new Route() {
		@Override
		public Object handle(Request request, Response response) throws Exception {
			long id = Long.parseLong(request.params(":id"));
			getManager().remove(id);
			return 200;
		}
	};
	
	public abstract Manager<?> getManager();
	
	public abstract Route add();
	
}
