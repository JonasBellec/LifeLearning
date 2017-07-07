package com.deckard.qlearning.predictor.deeplearning;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.factory.Nd4j;

import com.deckard.qlearning.predictor.IPredictor;
import com.deckard.qlearning.predictor.Transition;
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
	private boolean ready = false;

	private NormalizerStandardize normalizerMinMaxScaler = new NormalizerStandardize();

	private NeuralNetworkPredictor(Class<S> classState, Class<A> classAction, double discount) {
		this.stateSpace = StateSpace.getInstance(classState);
		this.actionSpace = ActionSpace.getInstance(classAction);
		this.discount = discount;
	}

	public NeuralNetworkPredictor(MultiLayerConfiguration multiLayerConfiguration, Class<S> classState,
			Class<A> classAction, double discount) {
		this(classState, classAction, discount);
		this.multiLayerNetworkSource = new MultiLayerNetwork(multiLayerConfiguration);
		this.multiLayerNetworkSource.init();
		this.multiLayerNetworkTarget = new MultiLayerNetwork(multiLayerConfiguration);
		this.multiLayerNetworkTarget.init();
		this.multiLayerNetworkTarget.setParams(multiLayerNetworkSource.params());
	}

	@Override
	public A predictAction(ObservationSpace<S> observationSpace) {
		if (ready) {
			INDArray arrayInput = createArrayFromObservedSpace(observationSpace);
			normalizerMinMaxScaler.transform(arrayInput);

			INDArray arrayOutput = multiLayerNetworkSource.output(arrayInput);

			int code = Nd4j.argMax(arrayOutput, Integer.MAX_VALUE).getInt(0);

			return actionSpace.decode(code);
		} else {
			return actionSpace.random();
		}
	}

	@Override
	public void train(List<Transition<S, A>> transitions) {

		List<ObservationSpace<S>> observationSpacesSource = new ArrayList<>();
		List<ObservationSpace<S>> observationSpacesTarget = new ArrayList<>();

		for (Transition<S, A> transition : transitions) {
			observationSpacesSource.add(transition.getObservationSpaceSource());
			observationSpacesTarget.add(transition.getObservationSpaceTarget());
		}

		INDArray arrayInputSource = createArrayFromObservedSpace(observationSpacesSource);
		INDArray arrayInputTarget = createArrayFromObservedSpace(observationSpacesTarget);

		DataSet dataSet = new DataSet(arrayInputSource, null);
		normalizerMinMaxScaler.fit(dataSet);
		ready = true;
		normalizerMinMaxScaler.transform(arrayInputSource);
		normalizerMinMaxScaler.transform(arrayInputTarget);

		INDArray arrayOutputSource = multiLayerNetworkSource.output(arrayInputSource);
		INDArray arrayOutputTarget = multiLayerNetworkTarget.output(arrayInputTarget);

		INDArray arrayMaxAction = Nd4j.argMax(arrayOutputTarget, 1);

		for (int i = 0; i < transitions.size(); i++) {
			Transition<S, A> transition = transitions.get(i);

			double rewardTarget;

			if (transition.getReward() < 0) {
				rewardTarget = transition.getReward();
			} else {
				rewardTarget = transition.getReward()
						+ discount * arrayOutputTarget.getDouble(i, arrayMaxAction.getInt(i));
			}

			arrayOutputSource.putScalar(i, transition.getAction().encode(), rewardTarget);
		}

		multiLayerNetworkSource.fit(arrayInputSource, arrayOutputSource);
		updateMultiLayerNetworkTarget();
	}

	public void updateMultiLayerNetworkTarget() {
		multiLayerNetworkTarget.setParams(multiLayerNetworkSource.params());
	}

	private INDArray createArrayFromObservedSpace(List<ObservationSpace<S>> observationSpaces) {
		INDArray array = Nd4j.create(observationSpaces.size(), stateSpace.size());
		int row = 0;

		for (ObservationSpace<S> observationSpace : observationSpaces) {
			array.putRow(row++, createArrayFromObservedSpace(observationSpace));
		}

		return array;
	}

	private INDArray createArrayFromObservedSpace(ObservationSpace<S> observationSpace) {

		double[] space = observationSpace.encode();
		INDArray array = Nd4j.create(space);
		return array;
	}

	public static void save(NeuralNetworkPredictor<?, ?> predictor, String configuration, String parameters) {
		try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Paths.get(parameters)))) {
			Nd4j.write(predictor.multiLayerNetworkSource.params(), dos);
			FileUtils.write(new File(configuration),
					predictor.multiLayerNetworkSource.getLayerWiseConfigurations().toJson());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <S extends Enum<S> & IState, A extends Enum<A> & IAction> NeuralNetworkPredictor<S, A> load(
			Class<S> classState, Class<A> classAction, String configuration, String parameters) {

		try (DataInputStream dis = new DataInputStream(Files.newInputStream(Paths.get(parameters)))) {

			MultiLayerConfiguration multiLayerConfiguration = MultiLayerConfiguration
					.fromJson(FileUtils.readFileToString(new File(configuration)));

			NeuralNetworkPredictor<S, A> neuralNetworkPredictor = new NeuralNetworkPredictor<>(classState, classAction,
					0.9);
			neuralNetworkPredictor.multiLayerNetworkSource = new MultiLayerNetwork(multiLayerConfiguration);
			neuralNetworkPredictor.multiLayerNetworkSource.init();
			neuralNetworkPredictor.multiLayerNetworkSource.setParameters(Nd4j.read(dis));
			neuralNetworkPredictor.multiLayerNetworkTarget = new MultiLayerNetwork(multiLayerConfiguration);
			neuralNetworkPredictor.multiLayerNetworkTarget.init();
			neuralNetworkPredictor.multiLayerNetworkTarget
					.setParams(neuralNetworkPredictor.multiLayerNetworkSource.params());

			return neuralNetworkPredictor;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
