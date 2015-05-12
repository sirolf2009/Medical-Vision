package org.medicalvision.server.core;

import static org.medicalvision.server.core.Constants.*;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class Configuration {
	
	private static Configuration Instance;

	public String SERVER_BASE_URI;
	public int SERVER_PORT;

	public int THRIFT_PORT;
	public String THRIFT_STORE_KEY;
	public String THRIFT_STORE_TRUST;
	
	private Configuration() {}
	
	public void loadServerConfig() {
    	try {
			XMLConfiguration config = new XMLConfiguration(CONFIG_SERVER);
			SERVER_BASE_URI = config.getString(CONFIG_BASE_URI, CONFIG_BASE_URI_DEFAULT);
			SERVER_PORT = config.getInt(CONFIG_PORT, CONFIG_PORT_DEFAULT_SERVER);
			config.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
    }
	
	public void loadThriftConfig() {
    	try {
			XMLConfiguration config = new XMLConfiguration(CONFIG_SERVER);
			THRIFT_PORT = config.getInt(CONFIG_PORT, CONFIG_PORT_DEFAULT_THRIFT);
			THRIFT_STORE_KEY = config.getString(CONFIG_STORE_KEY, CONFIG_STORE_KEY_DEFAULT);
			THRIFT_STORE_TRUST = config.getString(CONFIG_STORE_TRUST, CONFIG_STORE_TRUST_DEFAULT);
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
