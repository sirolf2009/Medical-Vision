package org.medical.vision.service.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.medicalvision.server.core.model.Employee;
import org.medicalvision.server.core.model.Task;
import org.medicalvision.server.core.model.TaskType;
import org.medicalvision.service.DatabaseManager;

public class TestDatabase {
	
	private DatabaseManager manager;

	@Before
	public void setUp() throws Exception {
		manager = new DatabaseManager();
	}

	@After
	public void tearDown() throws Exception {
	}

	/*@Test
	public void testEmployee() {
		Employee employee = new Employee();
		employee.setFirstName("Floris");
		employee.setLastName("Thijssen");
		long id = manager.pushEmployee(employee);
		assertNotSame(-1, id);
		assertEquals(employee, manager.pullEmployee(id));
	}*/

	@Test
	public void testTask() {
		Task task = new Task();
		task.setType(TaskType.SERVE_BREAKFAST.getName());
		long id = manager.pushTask(task);
		assertNotSame(-1, id);
		manager.removeTask(id);
	}

}
