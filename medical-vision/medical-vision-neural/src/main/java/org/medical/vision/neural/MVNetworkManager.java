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
	
	private MVNetwork lightNetwork;
	
	public MVNetworkManager() {
		this(new SparkConf().setMaster("local[4]").setAppName("Medical-Vision ANN"));
	}

	public MVNetworkManager(SparkConf conf) {
	    setSparkContext(new JavaSparkContext(conf));
	    
		List<Tuple2<Vector, Vector>> defaultLightNetwork = new ArrayList<Tuple2<Vector, Vector>>();
		defaultLightNetwork.add(new Tuple2<Vector, Vector>(new DenseVector(new double[] {0, 0}), new DenseVector(new double[] {0})));
		defaultLightNetwork.add(new Tuple2<Vector, Vector>(new DenseVector(new double[] {1, 0}), new DenseVector(new double[] {0})));
		defaultLightNetwork.add(new Tuple2<Vector, Vector>(new DenseVector(new double[] {0, 1}), new DenseVector(new double[] {1})));
		defaultLightNetwork.add(new Tuple2<Vector, Vector>(new DenseVector(new double[] {1, 1}), new DenseVector(new double[] {0})));
		lightNetwork = new MVNetwork(getSparkContext(), 2, 1);
		lightNetwork.setTrainingSet(defaultLightNetwork);
		lightNetwork.train();
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
