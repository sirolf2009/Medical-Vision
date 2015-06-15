package org.medicalvision.service;

import java.util.ArrayList;
import java.util.List;

import org.medicalvision.server.core.model.Employee;
import org.medicalvision.server.core.model.Patient;
import org.medicalvision.server.core.model.Room;
import org.medicalvision.server.core.model.SensorData;
import org.medicalvision.server.core.model.Task;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.sirolf2009.beantx.BeanTx;
import com.sirolf2009.beantx.BeanUtil;

public class DatabaseManager {

	private GraphDatabaseService service;
	private BeanTx beanTx;
	private final Manager<Task> taskManager;
	private final Manager<Employee> employeeManager;
	private final Manager<Patient> patientManager;
	private final Manager<SensorData> sensorManager;
	private final Manager<Room> roomManager;

	public DatabaseManager() {
		setService(new GraphDatabaseFactory().newEmbeddedDatabase("neo4j"));
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				getService().shutdown();
			}
		});
		setBeanTx(new BeanTx(getService()));
		taskManager = new Manager<Task>("Task");
		employeeManager = new Manager<Employee>("Employee");
		patientManager = new Manager<Patient>("Patient");
		sensorManager = new Manager<SensorData>("SensorData");
		roomManager = new Manager<Room>("Room");
	}

	public Room getRoomFromID(int roomID) {
		try(Transaction tx = getBeanTx().getService().beginTx()) {
			System.out.println("searching for room "+roomID);
			Room room = getRoomManager().pull(getBeanTx().getService().findNode(DynamicLabel.label("Room"), "roomID", roomID).getId());
			tx.success();
			return room;
		}
	}

	public Manager<Task> getTaskManager() {
		return taskManager;
	}

	public Manager<Employee> getEmployeeManager() {
		return employeeManager;
	}

	public Manager<Patient> getPatientManager() {
		return patientManager;
	}

	public Manager<Room> getRoomManager() {
		return roomManager;
	}

	public GraphDatabaseService getService() {
		return service;
	}

	public void setService(GraphDatabaseService service) {
		this.service = service;
	}

	public BeanTx getBeanTx() {
		return beanTx;
	}

	public void setBeanTx(BeanTx beanTX) {
		this.beanTx = beanTX;
	}

	public Manager<SensorData> getSensorManager() {
		return sensorManager;
	}

	@SuppressWarnings("unchecked")
	public class Manager<E> {

		private String[] labels;

		public Manager(String... labels) {
			this.labels = labels;
		}

		public long push(E bean) {
			return getBeanTx().pushBean(bean, labels);
		}

		public E pull(long ID) {
			return (E) getBeanTx().pullBean(ID);
		}

		public List<E> all() { //TODO placeholder until beanTx support
			List<E> objects = new ArrayList<E>();
			try(Transaction tx = getService().beginTx()) {
				String label = BeanUtil.combineLabels(labels);
				ResourceIterator<Node> itr = getService().findNodes(DynamicLabel.label(label.replace(":", "")));
				while(itr.hasNext()) {
					Node node = itr.next();
					objects.add((E) pull(node.getId()));
				}
				itr.close();
				tx.success();
			}
			return objects;
		}

		public void remove(long id) {
			getBeanTx().deleteBean(getBeanTx().pullBean(id));
		}

		public void remove(Object bean) {
			getBeanTx().deleteBean(bean);
		}

	}

}
