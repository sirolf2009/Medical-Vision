package org.medicalvision.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import spark.ModelAndView;
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
	
	public static String file(String location) throws IOException, URISyntaxException {
		return StringUtils.join(Files.readAllLines(new File(MVService.class.getClassLoader().getResource(location).toURI()).toPath()), "");
	}
	
	public static ModelAndView fileVelocity(String location, Object... pars) {
		Map<String, Object> model = new HashMap<String, Object>();
		if(pars.length %2 == 1) {
			throw new RuntimeException("The parameter array must be even. An odd paramater array was given");
		}
		System.out.println(pars.length);
		for(int i = 0; i < pars.length; i+=2) {
			if(pars[i] instanceof String) {
				System.out.println(pars[i]+" = "+pars[i+1]);
				model.put((String) pars[i], pars[i+1]);
			} else {
				throw new RuntimeException(pars[i] + " is at an even interval of the array, and must therefore be a String instance");
			}
		}
		return new ModelAndView(model, location);
	}

}
