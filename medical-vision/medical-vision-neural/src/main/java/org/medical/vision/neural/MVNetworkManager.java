package org.medical.vision.neural;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;
import scala.Tuple2;

public class MVNetworkManager {
	
	private JavaSparkContext sparkContext;
	
	private static MVNetworkManager instance;
	
	public MVNetworkManager() {
		this(new SparkConf().setMaster("spark://188.166.81.149:7077").setAppName("Medical-Vision ANN"));
		//this(new SparkConf().setMaster("local[4]").setAppName("Medical-Vision ANN"));
	}

	public MVNetworkManager(SparkConf conf) {
	    setSparkContext(new JavaSparkContext(conf));
	    
		List<Tuple2<Vector, Vector>> OR = new ArrayList<Tuple2<Vector, Vector>>();
		OR.add(new Tuple2<Vector, Vector>(new DenseVector(new double[] {0, 0}), new DenseVector(new double[] {0})));
		OR.add(new Tuple2<Vector, Vector>(new DenseVector(new double[] {1, 0}), new DenseVector(new double[] {1})));
		OR.add(new Tuple2<Vector, Vector>(new DenseVector(new double[] {0, 1}), new DenseVector(new double[] {1})));
		OR.add(new Tuple2<Vector, Vector>(new DenseVector(new double[] {1, 1}), new DenseVector(new double[] {1})));
		
		MVNetwork ORNetwork = new MVNetwork(getSparkContext(), 2, 1);
		ORNetwork.setTrainingSet(OR);
		ORNetwork.train();
		
		Vector output = ORNetwork.get(new DenseVector(new double[] {1, 0}));
		System.out.println(output);

		List<Tuple2<Vector, Vector>> newTraining = new ArrayList<Tuple2<Vector, Vector>>();
		newTraining.add(new Tuple2<Vector, Vector>(new DenseVector(new double[] {2, 2}), new DenseVector(new double[] {2})));
		ORNetwork.getTrainingSet().$plus$plus(getSparkContext().parallelize(newTraining).rdd());
		ORNetwork.train();

		output = ORNetwork.get(new DenseVector(new double[] {1, 0}));
		System.out.println(output);
		output = ORNetwork.get(new DenseVector(new double[] {2, 2}));
		System.out.println(output);
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
}
