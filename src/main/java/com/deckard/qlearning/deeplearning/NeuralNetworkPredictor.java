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
import com.deckard.qlearning.space.ActionSpace;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.space.StateSpace;

public class NeuralNetworkPredictor<S extends Enum<S> & IState, A extends Enum<A> & IAction>
		implements IPredictor<S, A> {
	private MultiLayerNetwork multiLayerNetworkSource;
	private MultiLayerNetwork multiLayerNetworkTarget;
	private double discount;
	private StateSpace<S> stateSpace;
	private ActionSpace<A> actionSpace;

	public NeuralNetworkPredictor(MultiLayerConfiguration conf, Class<S> classState, Class<A> classAction) {
		multiLayerNetworkSource = new MultiLayerNetwork(conf);
		multiLayerNetworkSource.init();
		multiLayerNetworkTarget = new MultiLayerNetwork(conf);
		multiLayerNetworkTarget.init();
		multiLayerNetworkTarget.setParams(multiLayerNetworkSource.params());

		this.stateSpace = StateSpace.getInstance(classState);
		this.actionSpace = ActionSpace.getInstance(classAction);
	}

	@Override
	public IAction predictAction(ObservationSpace<S> observationSpace) {
		INDArray arrayInput = createArrayFromObservedSpace(observationSpace);
		INDArray arrayOutput = multiLayerNetworkSource.output(arrayInput);

		return actionSpace.decode(Nd4j.argMax(arrayOutput, Integer.MAX_VALUE).getInt(0));
	}

	@Override
	public void train(List<Transition<S, A>> transitions) {

		INDArray arrayInputSource = createArrayFromObservedSpace(
				transitions.stream().map(p -> p.getObservationSpaceSource()));
		INDArray arrayInputTarget = createArrayFromObservedSpace(
				transitions.stream().map(p -> p.getObservationSpaceTarget()));

		INDArray arrayOutputSource = multiLayerNetworkSource.output(arrayInputSource);
		INDArray arrayOutputTarget = multiLayerNetworkTarget.output(arrayInputTarget);

		INDArray arrayMaxAction = Nd4j.argMax(arrayOutputTarget, 1);

		for (int i = 0; i < transitions.size(); i++) {
			Transition<S, A> transition = transitions.get(i);

			double rewardTarget = transition.getReward()
					+ discount * arrayOutputTarget.getDouble(i, arrayMaxAction.getInt(i));

			arrayOutputSource.putScalar(i, transition.getAction().encode(), rewardTarget);
		}

		multiLayerNetworkSource.fit(arrayInputSource, arrayOutputSource);
	}

	private INDArray createArrayFromObservedSpace(Stream<ObservationSpace<S>> observationSpaces) {
		INDArray array = Nd4j.create((int) observationSpaces.count(), stateSpace.size());
		int row = 0;

		for (ObservationSpace<S> observationSpace : observationSpaces.collect(Collectors.toList())) {
			array.putRow(row++, createArrayFromObservedSpace(observationSpace));
		}

		return array;
	}

	private INDArray createArrayFromObservedSpace(ObservationSpace<S> observationSpace) {
		return Nd4j.create(observationSpace.encode());
	}
}
