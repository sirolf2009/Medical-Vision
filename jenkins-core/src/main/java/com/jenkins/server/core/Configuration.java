package com.jenkins.server.core;

import static com.jenkins.server.core.Constants.*;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class Configuration {
	
	private static Configuration Instance;

	public String SERVER_BASE_URI;
	public int SERVER_PORT;
	
	private Configuration() {}
	
	public void loadServerConfig() {
    	try {
			XMLConfiguration config = new XMLConfiguration(CONFIG);
			SERVER_BASE_URI = config.getString(CONFIG_BASE_URI, CONFIG_BASE_URI_DEFAULT);
			SERVER_PORT = config.getInt(CONFIG_PORT, CONFIG_PORT_DEFAULT);
			config.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
    }
	
	public static Configuration getInstance() {
		if(Instance == null) {
			Instance = new Configuration();
		}
		return Instance;
	}
	
}
