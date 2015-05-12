package org.medicalvision.service.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.medicalvision.server.core.Configuration;
import org.medicalvision.server.core.model.Task;

import com.medicalvision.core.thrift.Patient;
import com.medicalvision.core.thrift.MedicalVision;

public class Client {
	
	private TTransport transport;
	private MedicalVision.Client client;
	private String ip;

	public Client(String ip) {
		setIp(ip);
		createTransport();
	}
	
	public void createTransport() {
		try {
			Configuration config = Configuration.getInstance();
			config.loadThriftConfig();
			TSSLTransportParameters params = new TSSLTransportParameters();
			params.setTrustStore(config.THRIFT_STORE_TRUST, "thrift", "SunX509", "JKS");
			setTransport(TSSLTransportFactory.getClientSocket(getIp(), config.THRIFT_PORT, 0, params));
			setClient(new MedicalVision.Client(new TBinaryProtocol(transport)));
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyOfTask(Task task) throws TException {
		Patient patient = new Patient(task.getPatient().getFirstName(), task.getPatient().getLastName());
		com.medicalvision.core.thrift.Task thriftTask = new com.medicalvision.core.thrift.Task(task.getType(), patient);
		getClient().startTask(thriftTask);
	}

	public TTransport getTransport() {
		return transport;
	}

	public void setTransport(TTransport transport) {
		this.transport = transport;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public MedicalVision.Client getClient() {
		return client;
	}

	public void setClient(MedicalVision.Client client) {
		this.client = client;
	}

}
