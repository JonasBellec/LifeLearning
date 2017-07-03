package com.deckard.lifelearning;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import com.deckard.lifelearning.model.Action;
import com.deckard.lifelearning.model.State;
import com.deckard.lifelearning.universe.RealUniverse;
import com.deckard.qlearning.deeplearning.NeuralNetworkPredictor;
import com.deckard.qlearning.policy.IPolicy;
import com.deckard.qlearning.policy.PolicyConfiguration;
import com.deckard.qlearning.policy.QLearningPolicy;
import com.deckard.qlearning.space.ActionSpace;
import com.deckard.qlearning.space.StateSpace;

public class LifeLearning {

	private LifeLearning() {

	}

	public static void main(String[] args) {

		int seed = 123;
		double learningRate = 0.01;

		int numHiddenNodes = 20;

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).iterations(1)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).learningRate(learningRate)
				.updater(Updater.NESTEROVS).momentum(0.9).list()
				.layer(0,
						new DenseLayer.Builder().nIn(StateSpace.getInstance(State.class).size()).nOut(numHiddenNodes)
								.weightInit(WeightInit.XAVIER).activation(Activation.RELU).build())
				.layer(1,
						new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD).weightInit(WeightInit.XAVIER)
								.activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).nIn(numHiddenNodes)
								.nOut(ActionSpace.getInstance(Action.class).size()).build())
				.pretrain(false).backprop(true).build();

		PolicyConfiguration<State, Action> policyConfiguration = new PolicyConfiguration<>(State.class, Action.class);

		IPolicy<State, Action> policy = new QLearningPolicy<>(policyConfiguration,
				new NeuralNetworkPredictor<>(conf, State.class, Action.class));

		RealUniverse environment = new RealUniverse(policy, 10000);

		Integer days = 2;

		for (int i = 0; i < 24 * days; i++) {
			environment.step();
		}
	}
}
