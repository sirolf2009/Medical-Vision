package org.medical.vision.neural;

import java.util.List;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.ann.FeedForwardModel;
import org.apache.spark.mllib.ann.FeedForwardTrainer;
import org.apache.spark.mllib.ann.Topology;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.RDD;

import scala.Tuple2;

public class MVNetwork {
	
	private JavaSparkContext sparkContext;
	private RDD<Tuple2<Vector, Vector>> trainingSet;
	private Vector initialWeights;
	private int inputSize;
	private int outputSize;
	private int hiddenLayerSize;
	private final Topology topology;
	private int numIterations;
	private FeedForwardModel neuralNetwork;

	public MVNetwork(JavaSparkContext sparkContext, int inputSize, int outputSize) {
		this(sparkContext, null, inputSize, outputSize);
	}

	public MVNetwork(JavaSparkContext sparkContext, RDD<Tuple2<Vector, Vector>> trainingSet, int inputSize, int outputSize) {
		setSparkContext(sparkContext);
		setTrainingSet(trainingSet);
		setInputSize(inputSize);
		setOutputSize(outputSize);
		setHiddenLayerSize(inputSize*2);
		
		topology = Topology.multiLayerPerceptron(new int[] {2, 4, 1}, false);
		FeedForwardModel model = FeedForwardModel.apply(getTopology(), 4567);
		setInitialWeights(model.weights());
		
		setNumIterations(200);
	}
	
	public void train() {
		if(getTrainingSet() == null || getTrainingSet().count() == 0) {
			throw new RuntimeException("The training set is null or empty");
		}
		getTrainingSet().cache();
		FeedForwardTrainer trainer = new FeedForwardTrainer(getTopology(), 2, 1);
		trainer.setWeights(getInitialWeights());
		trainer.LBFGSOptimizer().setNumIterations(getNumIterations());
		setNeuralNetwork(trainer.train(getTrainingSet()));
	}
	
	public Vector get(Vector input) {
		if(getNeuralNetwork() == null) {
			throw new RuntimeException("You need to call train() before calling get()");
		}
		return getNeuralNetwork().predict(input);
	}

	public JavaSparkContext getSparkContext() {
		return sparkContext;
	}

	public void setSparkContext(JavaSparkContext sparkContext) {
		this.sparkContext = sparkContext;
	}

	public RDD<Tuple2<Vector, Vector>> getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(RDD<Tuple2<Vector, Vector>> trainingSet) {
		this.trainingSet = trainingSet;
	}

	public void setTrainingSet(List<Tuple2<Vector, Vector>> trainingSet) {
		setTrainingSet(getSparkContext().parallelize(trainingSet).rdd());
	}

	public Vector getInitialWeights() {
		return initialWeights;
	}

	public void setInitialWeights(Vector initialWeights) {
		this.initialWeights = initialWeights;
	}

	public int getInputSize() {
		return inputSize;
	}

	public void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}

	public int getOutputSize() {
		return outputSize;
	}

	public void setOutputSize(int outputSize) {
		this.outputSize = outputSize;
	}

	public int getHiddenLayerSize() {
		return hiddenLayerSize;
	}

	public void setHiddenLayerSize(int hiddenLayerSize) {
		this.hiddenLayerSize = hiddenLayerSize;
	}

	public Topology getTopology() {
		return topology;
	}

	public int getNumIterations() {
		return numIterations;
	}

	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	public FeedForwardModel getNeuralNetwork() {
		return neuralNetwork;
	}

	public void setNeuralNetwork(FeedForwardModel neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	}

}
