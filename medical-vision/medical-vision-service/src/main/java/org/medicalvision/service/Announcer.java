package org.medicalvision.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.medicalvision.server.core.Constants;

import com.cjsavage.java.net.discovery.ServiceAnnouncer;
import com.cjsavage.java.net.discovery.ServiceInfo;

public class Announcer implements Runnable {

	private ServiceAnnouncer serviceAnnouncer;

	public Announcer() {
		initAnnouncer();
	}
	
	@Override
	public void run() {
		if (serviceAnnouncer.isListening()) {
			serviceAnnouncer.stopListening();
		}
	}

	private void initAnnouncer() {
		ServiceAnnouncer announcer = new ServiceAnnouncer();
		ServiceInfo service = new ServiceInfo("Medical Vision Service", Constants.SERVICE_ID, getLocalIp(), 12345, false);
		announcer.addService(service);
		announcer.startListening();
		serviceAnnouncer = announcer;
	}

	private String getLocalIp() {
		String ip;
		try{
			InetAddress ia = InetAddress.getLocalHost();
			ip = ia.getHostAddress();
		} catch (UnknownHostException e) { 
			ip = "127.0.0.1";
		}
		return ip;
	}

	public ServiceAnnouncer getServiceAnnouncer() {
		return serviceAnnouncer;
	}

	public void setServiceAnnouncer(ServiceAnnouncer serviceAnnouncer) {
		this.serviceAnnouncer = serviceAnnouncer;
	}
}