package org.medicalvision.server.core;

public class Constants {

	public static final String CONFIG_SERVER = "server.xml";
	public static final String CONFIG_THRIFT = "thrift.xml";
	public static final String CONFIG_KRYO = "kryonet.xml";

	public static final String CONFIG_PORT = "port";
	public static final int CONFIG_PORT_DEFAULT_SERVER = 8080;
	public static final int CONFIG_PORT_DEFAULT_THRIFT = 8082;
	
	public static final String CONFIG_BASE_URI = "base_uri";
	public static final String CONFIG_BASE_URI_DEFAULT = "http://localhost:8080/medical-vision";

	public static final String CONFIG_STORE_TRUST = "truststore";
	public static final String CONFIG_STORE_TRUST_DEFAULT = "truststore.jks";

	public static final String CONFIG_STORE_KEY = "keystore";
	public static final String CONFIG_STORE_KEY_DEFAULT = "keystore.jks";

	public static final String CONFIG_PORT_START = "portStart";
	public static final int CONFIG_PORT_START_DEFAULT_KRYO = 8100;
	public static final String CONFIG_PORT_END = "portEnd";
	public static final int CONFIG_PORT_END_DEFAULT_KRYO = 8100;
	
    public static final String SERVICE_ID = "45e643d7d66ffeb36ecebd0fd28c043acc02de82e6e92872758515b9f4fd2d70c4d1187db8ec68989f7c1d8a315f58ca03b86567651dab94b40f7a534af870f6";

}
