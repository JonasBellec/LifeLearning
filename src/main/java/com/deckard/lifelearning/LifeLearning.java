package com.deckard.lifelearning;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import com.deckard.lifelearning.model.Action;
import com.deckard.lifelearning.model.Agent;
import com.deckard.lifelearning.model.State;
import com.deckard.lifelearning.universe.RealUniverse;
import com.deckard.qlearning.policy.IPolicy;
import com.deckard.qlearning.policy.PolicyConfiguration;
import com.deckard.qlearning.policy.QLearningPolicy;
import com.deckard.qlearning.predictor.deeplearning.NeuralNetworkPredictor;
import com.deckard.qlearning.space.ActionSpace;
import com.deckard.qlearning.space.StateSpace;

public class LifeLearning {

	private static Logger logger = Logger.getLogger(LifeLearning.class.getSimpleName());

	private LifeLearning() {

	}

	public static void main(String[] args) {

		NeuralNetworkPredictor<State, Action> neuralNetworkPredictor;

		try {
			neuralNetworkPredictor = NeuralNetworkPredictor.load(State.class, Action.class, "configuration.json",
					"parameters.txt");
		} catch (Exception e) {
			neuralNetworkPredictor = createNewNeuralNetworkPredictor();
		}

		// UIServer uiServer = UIServer.getInstance();
		// StatsStorage statsStorage = new InMemoryStatsStorage(); // Alternative: new FileStatsStorage(File), for
		// uiServer.attach(statsStorage);
		// neuralNetworkPredictor.getMultiLayerNetworkSource().setListeners(new StatsListener(statsStorage));
		// neuralNetworkPredictor.getMultiLayerNetworkTarget().setListeners(new StatsListener(statsStorage));

		PolicyConfiguration<State, Action> policyConfiguration = new PolicyConfiguration<>(State.class, Action.class,
				100, 0.2);
		IPolicy<State, Action> policy = new QLearningPolicy<>(policyConfiguration, neuralNetworkPredictor);

		for (int k = 0; k < 50; k++) {
			RealUniverse realUniverse = new RealUniverse(policy, 1000);

			Integer days = 10;

			for (int i = 0; i < 24 * days; i++) {
				realUniverse.step();

				if (i % 24 == 0) {
					int alive = log(realUniverse, i);

					if (alive <= 0) {
						break;
					}
				}
			}

			log(realUniverse, 240);

			NeuralNetworkPredictor.save(neuralNetworkPredictor, "configuration.json", "parameters.txt");
		}
	}

	private static int log(RealUniverse realUniverse, int tick) {
		int alive = 0;
		int dead = 0;
		double rewardTotal = 0;

		for (Agent agent : realUniverse.getAgents()) {
			if (agent.isAlive()) {
				alive++;
				rewardTotal += agent.computeReward();
			} else {
				dead++;
			}
		}

		double rewardAverage = 0;

		if (alive != 0) {
			rewardAverage = rewardTotal / alive;
		}

		logger.log(Level.INFO, String.format("%d/%d => alive : %d, dead : %d, averageReward : %f", tick / 24, tick % 24,
				alive, dead, rewardAverage));

		return alive;
	}

	private static NeuralNetworkPredictor<State, Action> createNewNeuralNetworkPredictor() {
		int seed = 123;
		double learningRate = 0.01;

		int numHiddenNodes = 20;

		// MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).iterations(1)
		// .activation(Activation.TANH).weightInit(WeightInit.XAVIER)
		// .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).learningRate(learningRate)
		// .updater(Updater.NESTEROVS).momentum(0.9).regularization(true).l2(1e-4).list()
		// .layer(0,
		// new DenseLayer.Builder().nIn(StateSpace.getInstance(State.class).size()).nOut(numHiddenNodes)
		// .weightInit(WeightInit.XAVIER).activation(Activation.RELU).build())
		// .layer(1,
		// new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes).weightInit(WeightInit.XAVIER)
		// .activation(Activation.RELU).build())
		// .layer(2,
		// new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
		// .activation(Activation.SOFTMAX).nIn(numHiddenNodes)
		// .nOut(ActionSpace.getInstance(Action.class).size()).build())
		// .backprop(true).pretrain(false).build();

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).iterations(1)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).learningRate(learningRate).list()
				.layer(0,
						new DenseLayer.Builder().nIn(StateSpace.getInstance(State.class).size()).nOut(numHiddenNodes)
								.weightInit(WeightInit.XAVIER).activation(Activation.RELU).build())
				.layer(1,
						new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD).weightInit(WeightInit.XAVIER)
								.activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).nIn(numHiddenNodes)
								.nOut(ActionSpace.getInstance(Action.class).size()).build())
				.pretrain(false).backprop(true).build();

		return new NeuralNetworkPredictor<>(conf, State.class, Action.class, 0.8);
	}
}
