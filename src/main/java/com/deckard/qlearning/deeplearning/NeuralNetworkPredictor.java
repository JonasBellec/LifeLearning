package com.deckard.qlearning.deeplearning;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.deckard.qlearning.policy.IPredictor;
import com.deckard.qlearning.policy.Transition;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IObservationSpace;
import com.deckard.qlearning.space.IStateSpace;

public class NeuralNetworkPredictor<T extends IStateSpace, B extends IActionSpace> implements IPredictor<T, B> {
	private MultiLayerNetwork multiLayerNetworkSource;
	private MultiLayerNetwork multiLayerNetworkTarget;
	private double discount;

	NeuralNetworkPredictor(MultiLayerConfiguration conf) {
		multiLayerNetworkSource = new MultiLayerNetwork(conf);
		multiLayerNetworkSource.init();
		multiLayerNetworkTarget = new MultiLayerNetwork(conf);
		multiLayerNetworkTarget.init();
		multiLayerNetworkTarget.setParams(multiLayerNetworkSource.params());
	}

	@Override
	public IAction predictAction(IObservationSpace<T> observationSpace) {
		INDArray arrayInput = createArrayFromObservedSpace(observationSpace);
		INDArray arrayOutput = multiLayerNetworkSource.output(arrayInput);

		return Nd4j.argMax(arrayOutput, Integer.MAX_VALUE).getInt(0);
	}

	@Override
	public void train(List<Transition<T>> transitions) {

		INDArray arrayInputSource = createArrayFromObservedSpace(
				transitions.stream().map(p -> p.getObservationSpaceSource()));
		INDArray arrayInputTarget = createArrayFromObservedSpace(
				transitions.stream().map(p -> p.getObservationSpaceTarget()));

		INDArray arrayOutputSource = multiLayerNetworkSource.output(arrayInputSource);
		INDArray arrayOutputTarget = multiLayerNetworkTarget.output(arrayInputTarget);

		INDArray arrayMaxAction = Nd4j.argMax(arrayOutputTarget, 1);

		for (int i = 0; i < transitions.size(); i++) {
			Transition<T> transition = transitions.get(i);

			double rewardTarget = transition.getReward()
					+ discount * arrayOutputTarget.getDouble(i, arrayMaxAction.getInt(i));

			arrayOutputSource.putScalar(i, transition.getAction().encode(), rewardTarget);
		}

		multiLayerNetworkSource.fit(arrayInputSource, arrayOutputSource);
	}

	private INDArray createArrayFromObservedSpace(Stream<IObservationSpace<T>> observationSpaces) {
		INDArray array = Nd4j.create(observationSpaces.count(), InputLength);
		int row = 0;

		for (IObservationSpace<T> observationSpace : observationSpaces.collect(Collectors.toList())) {
			array.putRow(row++, createArrayFromObservedSpace(observationSpace));
		}

		return array;
	}

	private INDArray createArrayFromObservedSpace(IObservationSpace<T> observationSpace) {
		return Nd4j.create(observationSpace.encode());
	}
}
