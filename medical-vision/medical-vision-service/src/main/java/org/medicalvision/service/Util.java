package org.medicalvision.service;

import spark.Request;

public class Util {

	public static int paramAsInt(Request request, String param) {
		return Integer.parseInt(request.params(param));
	}

	public static double paramAsDouble(Request request, String param) {
		return Double.parseDouble(request.params(param));
	}

	public static long paramAsLong(Request request, String param) {
		return Long.parseLong(request.params(param));
	}

}
