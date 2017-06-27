package com.deckard.lifelearning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.inject.Instance;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import com.deckard.lifelearning.policy.IPolicy;
import com.deckard.lifelearning.policy.InitPolicy;

public class Init {
	public static void main(String[] args) {

		int seed = 123;
		double learningRate = 0.01;
		int batchSize = 50;
		int nEpochs = 30;

		int numInputs = 2;
		int numOutputs = 2;
		int numHiddenNodes = 20;

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).iterations(1)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).learningRate(learningRate)
				.updater(Updater.NESTEROVS).momentum(0.9).list()
				.layer(0,
						new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes).weightInit(WeightInit.XAVIER)
								.activation(Activation.RELU).build())
				.layer(1,
						new OutputLayer().Builder(LossFunction.NEGATIVELOGLIKELIHOOD).weightInit(WeightInit.XAVIER)
								.activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).nIn(numHiddenNodes)
								.nOut(numOutputs).build())
				.pretrain(false).backprop(true).build();

		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		model.setListeners(new ScoreIterationListener(10)); // Print score every 10 parameter updates

		IPolicy policy = new InitPolicy();

		Environment environment = new Environment(policy, 10000);

		Integer days = 2;

		for (int i = 0; i < 24 * days; i++) {
			environment.tick();
		}

		Map<Action, Instances> mapFusionned = new HashMap<>();

		for (Agent agent : environment.getListAgent()) {
			Map<Action, Instances> mapInstances = ((InitPolicy) environment.getPolicy()).getData().get(agent);

			for (Entry<Action, Instances> entry : mapInstances.entrySet()) {
				Attribute attribute = entry.getValue().attribute("reward");
				entry.getValue().setClass(attribute);

				for (Instance instance : entry.getValue()) {
					instance.setValue(attribute, agent.computeReward());
				}

				Instances instances = mapFusionned.get(entry.getKey());

				if (instances == null) {
					mapFusionned.put(entry.getKey(), entry.getValue());
				} else {
					instances.addAll(entry.getValue());
				}
			}
		}

		try {
			for (Entry<Action, Instances> entry : mapFusionned.entrySet()) {
				BufferedWriter writer = new BufferedWriter(new FileWriter("" + entry.getKey().toString() + ".arff"));

				writer.write(entry.getValue().toString());
				writer.newLine();
				writer.flush();
				writer.close();

				if (!entry.getValue().isEmpty()) {
					LinearRegression classifier = new LinearRegression();
					classifier.buildClassifier(entry.getValue());

					SerializationHelper.write("" + entry.getKey().toString() + ".model", classifier);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
