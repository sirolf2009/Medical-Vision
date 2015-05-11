package org.medicalvision.service;

import java.util.ArrayList;
import java.util.List;

import org.medicalvision.server.core.model.Employee;
import org.medicalvision.server.core.model.Task;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.sirolf2009.beantx.BeanTX;

public class DatabaseManager {

	private GraphDatabaseService service;
	private BeanTX beanTX;

	public DatabaseManager() {
		service = new GraphDatabaseFactory().newEmbeddedDatabase("neo4j");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				service.shutdown();
			}
		});
		beanTX = new BeanTX(service);
	}

	public long pushEmployee(Employee employee) {
		return beanTX.pushBean(employee, "Employee");
	}
	
	public Employee pullEmployee(long id) {
		return (Employee) beanTX.pullBean(id);
	}

	public long pushTask(Task task) {
		return beanTX.pushBean(task, "Task");
	}
	
	public Task pullTask(long id) {
		return (Task) beanTX.pullBean(id);
	}
	
	public void removeTask(long id) {
		try(Transaction tx = service.beginTx()) {
			service.getNodeById(id).delete();
		}
	}

	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		try(Transaction tx = service.beginTx()) {
			ResourceIterator<Node> itr = service.findNodes(DynamicLabel.label("Employee"));
			while(itr.hasNext()) {
				Node node = itr.next();
				employees.add(pullEmployee(node.getId()));
			}
			itr.close();
		}
		return employees;
	}

}
