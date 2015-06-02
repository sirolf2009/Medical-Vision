package org.medical.vision.kryonet.android;

import java.io.IOException;
import java.net.InetAddress;

import org.medicalvision.server.core.kryo.PacketIP;
import org.medicalvision.server.core.model.Employee;
import org.medicalvision.server.core.model.Patient;
import org.medicalvision.server.core.model.Task;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {

    	Client client = new Client();
    	client.start();

    	
    	
    	
    	client.getKryo().register(PacketIP.class);
    	client.getKryo().register(Task.class);
    	client.getKryo().register(Employee.class);
    	client.getKryo().register(Patient.class);
    	
    	PacketIP request = new PacketIP();
    	
    	Employee employee = new Employee();
    	request.setEmployee(employee);
    	employee.setFirstName("Gok");
    	employee.setLastName("Kac");

    	request.setMobileIP("iets");

    	InetAddress address = client.discoverHost(8101, 8000);
    	System.out.println(address);
    	client.connect(5000, address, 8100, 8101);

    	client.addListener(new Listener() {
 	       public void received (Connection connection, Object object) {
 	    	   System.out.println(object);
 	          if (object instanceof Task) {
 	             Task response = (Task)object;
 	             System.out.println(response.toString());
 	          }
 	       }
 	    });

    	
    	try {
    		client.sendTCP(request);
    	} catch (Exception e) {
    		System.out.println(e.getStackTrace());
    		System.out.println(e.getMessage());
    	}
    	
    	while (true) {
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	// client.close();
    }
    
   
}




