namespace java com.medicalvision.core.thrift

struct Task {
	1: required string name;
	2: required Patient patient;
}

struct Patient {
	1: required string firstName;
	2: required string lastName;
}

service medicalVision {

	void startTask(1: Task task)
}
