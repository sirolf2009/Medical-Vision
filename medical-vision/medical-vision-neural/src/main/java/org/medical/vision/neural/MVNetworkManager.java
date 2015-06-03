package org.medical.vision.neural;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;
import org.medicalvision.server.core.model.NeuralInput;
import org.medicalvision.server.core.model.SensorData;
import org.medicalvision.server.core.model.Task;
import org.medicalvision.server.core.model.TaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple2;

public class MVNetworkManager {
	
	private JavaSparkContext sparkContext;
	
	private static MVNetworkManager instance;

	public static final double PANIC = 1;
	public static final double WANDERER = 2;
	
	private static final Logger log = LoggerFactory.getLogger(MVNetwork.class.getSimpleName());
	
	private Map<Double, MVNetwork> networks;
	
	public MVNetworkManager() {
		this(new SparkConf().setMaster("local[4]").setAppName("Medical-Vision ANN"));
	}

	public MVNetworkManager(SparkConf conf) {
	    setSparkContext(new JavaSparkContext(conf));
	    
	    log.info("Creating initial networks...");
	    networks = new HashMap<Double, MVNetwork>();
	    for(double[] set : defaultTraining) {
	    	double[] unparsedInput = Arrays.copyOf(set, set.length-1);
	    	List<Integer> indexes = new ArrayList<Integer>();
	    	for(int i = 0; i < unparsedInput.length; i++) {
	    		if(unparsedInput[i] != -1) {
	    			indexes.add(i);
	    		}
	    	}
	    	double[] input = new double[indexes.size()];
	    	for(int i = 0; i < indexes.size(); i++) {
	    		input[i] = set[indexes.get(i)];
	    	}
	    	
	    	double[][] options;
	    	if(indexes.size() == 1) {
	    		options = singleInput;
	    	} else if(indexes.size() == 2) {
	    		options = doubleInput;
	    	} else if(indexes.size() == 3) {
	    		options = tripleInput;
	    	} else {
	    		throw new RuntimeException(set[set.length-1]+" has an invalid amount of options: "+indexes.size());
	    	}
	    	
	    	double[][] trainingArraySet = new double[options.length][];
	    	for(int i = 0; i < options.length; i++) {
	    		double[] option = options[i];
	    		double[] trainingArray = Arrays.copyOf(option, option.length+1);
	    		if(Arrays.equals(option, input)) {
	    			trainingArray[trainingArray.length-1] = 1;
	    		}
	    		trainingArraySet[i] = trainingArray;
	    	}
	    	
    		List<Tuple2<Vector, Vector>> trainingSet = new ArrayList<Tuple2<Vector, Vector>>();
	    	for(double[] array : trainingArraySet) {
	    		trainingSet.add(new Tuple2<Vector, Vector>(new DenseVector(Arrays.copyOf(array, array.length-1)), new DenseVector(new double[] {array[array.length-1]})));
	    	}
	    	MVNetwork network = new MVNetwork(getSparkContext(), 4, 1, indexes);
			network.setTrainingSet(trainingSet);
			network.train();
			networks.put(set[set.length-1], network);
	    }
	}
	
	public List<Task> process(List<SensorData> data) {
		return process(getNeuralInput(data));
	}
	
	public List<Task> process(NeuralInput neuralInput) {
		Vector input = new DenseVector(neuralInput.toArray());
		List<Task> tasks = new ArrayList<Task>();
		for(Double emergency : networks.keySet()) {
			if(networks.get(emergency).get(input).toArray()[0] == 1) {
				if(emergency == PANIC) {
					tasks.add(createTask(TaskType.EMERGENCY_UNKNOWN));
				} else if(emergency == WANDERER) {
					tasks.add(createTask(TaskType.EMERGENCY_PERSON_WANDERING));
				}
			}
		}
		return tasks;
	}
	
	private Task createTask(TaskType type) {
		Task task = new Task();
		task.setType(TaskType.EMERGENCY_UNKNOWN.name());
		return task;
	}
	
	/**
	 * Holy fucking map reduce
	 * @param data
	 * @return NeuralInput
	 */
	public NeuralInput getNeuralInput(List<SensorData> data) {
		JavaRDD<SensorData> rdd = getSparkContext().parallelize(data);
		
		JavaPairRDD<Integer, Tuple2<Long, SensorData>> sorted = rdd.mapToPair(new PairFunction<SensorData, Long, SensorData>() {
			private static final long serialVersionUID = -5219123016459117182L;
			@Override
			public Tuple2<Long, SensorData> call(SensorData v1) throws Exception {
				return new Tuple2<Long, SensorData>(System.currentTimeMillis()-v1.getTimestamp(), v1);
			}
		}).mapToPair(new PairFunction<Tuple2<Long,SensorData>, Integer, Tuple2<Long, SensorData>>() {
			private static final long serialVersionUID = -3536669254607537519L;
			@Override
			public Tuple2<Integer, Tuple2<Long, SensorData>> call(Tuple2<Long, SensorData> t) throws Exception {
				return new Tuple2<Integer, Tuple2<Long,SensorData>>(t._2.getSensorID(), t);
			}
		});
		
		Function2<Tuple2<Long,SensorData>, Tuple2<Long,SensorData>, Tuple2<Long,SensorData>> findLatest = new Function2<Tuple2<Long,SensorData>, Tuple2<Long,SensorData>, Tuple2<Long,SensorData>>() {
			private static final long serialVersionUID = -1845790141059242274L;
			@Override
			public Tuple2<Long, SensorData> call(Tuple2<Long, SensorData> v1, Tuple2<Long, SensorData> v2) throws Exception {
				return v1._1 < v2._1 ? v1 : v2;
			}
		};
		
		JavaPairRDD<Integer, Tuple2<Long, SensorData>> latestRDD = sorted.reduceByKey(findLatest);
		Map<Integer, Long> TSLS = sorted.subtract(latestRDD).reduceByKey(findLatest).mapToPair(new PairFunction<Tuple2<Integer,Tuple2<Long,SensorData>>, Integer, Long>() {
			private static final long serialVersionUID = -3536669254607537519L;
			@Override
			public Tuple2<Integer, Long> call(Tuple2<Integer, Tuple2<Long, SensorData>> t) throws Exception {
				return new Tuple2<Integer, Long>(t._1, t._2._1);
			}
		}).collectAsMap();
		
		Map<Integer, Tuple2<Long, SensorData>> latest = latestRDD.collectAsMap();
		
		NeuralInput input = new NeuralInput();
		input.setDoor1(latest.get(0)._2.getValue());
		input.setDoor1TSLS(TSLS.get(0));
		return input;
	}
	
	public static MVNetworkManager getInstance() {
		if(instance == null) {
			instance = new MVNetworkManager();
		}
		return instance;
	}

	public static void main(String[] args) {
		new MVNetworkManager();
	}

	public JavaSparkContext getSparkContext() {
		return sparkContext;
	}

	public void setSparkContext(JavaSparkContext sparkContext) {
		this.sparkContext = sparkContext;
	}
	
	private static final double[][] defaultTraining = new double[][] {
		//				TOD		motion1		motion1'	motion2		motion2'	infra1	infra1'	infra2	infra2'	door1	door1'	door2	door2'	light1	light1'	light2	light2'	panic	result
		new double[] {	-1,		-1,			-1,			-1,			-1,			-1,		-1,		-1,		-1,		-1,		-1,		-1,		-1,		-1,		-1,		-1,		-1,		1,		PANIC},
		new double[] {	0,		-1,			-1,			-1,			-1,			-1,		-1,		-1,		-1,		0,		-1,		-1,		-1,		-1,		-1,		-1,		-1,		-1,		WANDERER}
	};
	
	private static final double[][] singleInput = new double[][] {
		new double[] {0},
		new double[] {1}
	};
	
	private static final double[][] doubleInput = new double[][] {
		new double[] {0, 0},
		new double[] {0, 1},
		new double[] {1, 0},
		new double[] {1, 1}
	};
	
	private static final double[][] tripleInput = new double[][] {
		new double[] {0, 0, 0},
		new double[] {0, 0, 1},
		new double[] {0, 1, 0},
		new double[] {0, 1, 1},
		new double[] {1, 0, 0},
		new double[] {1, 0, 1},
		new double[] {1, 1, 0},
		new double[] {1, 1, 1}
	};
}
