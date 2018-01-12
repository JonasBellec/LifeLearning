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
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.buffer.util.DataTypeUtil;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.NDArrayFactory;
import org.nd4j.linalg.factory.Nd4j;

import com.deckard.qlearning.predictor.Experience;
import com.deckard.qlearning.predictor.IPredictor;
import com.deckard.qlearning.space.ActionSpace;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.space.StateSpace;

public class NeuralNetworkPredictor<S extends Enum<S> & IState, A extends Enum<A> & IAction>
		implements IPredictor<S, A> {
	private MultiLayerNetwork multiLayerNetworkTraining;
	private MultiLayerNetwork multiLayerNetworkFrozen;
	private StateSpace<S> stateSpace;
	private ActionSpace<A> actionSpace;

	int action1 = 0;
	int action2 = 0;
	int action3 = 0;

	private NeuralNetworkPredictor(Class<S> classState, Class<A> classAction) {
		this.stateSpace = StateSpace.getInstance(classState);
		this.actionSpace = ActionSpace.getInstance(classAction);

		DataTypeUtil.setDTypeForContext(DataBuffer.Type.DOUBLE);

		NDArrayFactory factory = Nd4j.factory();
		factory.setDType(DataBuffer.Type.DOUBLE);
	}

	public NeuralNetworkPredictor(MultiLayerConfiguration multiLayerConfiguration, Class<S> classState,
			Class<A> classAction) {
		this(classState, classAction);
		this.multiLayerNetworkTraining = new MultiLayerNetwork(multiLayerConfiguration);
		this.multiLayerNetworkTraining.init();
		this.multiLayerNetworkFrozen = new MultiLayerNetwork(multiLayerConfiguration);
		this.multiLayerNetworkFrozen.init();
		this.multiLayerNetworkFrozen.setParams(multiLayerNetworkTraining.params());
	}

	@Override
	public A predict(ObservationSpace<S> observationSpace) {
		INDArray arrayInput = createArrayFromObservedSpace(observationSpace);
		INDArray arrayOutput = multiLayerNetworkFrozen.output(arrayInput);
		int code = Nd4j.argMax(arrayOutput, Integer.MAX_VALUE).getInt(0);
		return actionSpace.decode(code);
	}

	@Override
	public void updateMultiLayerNetworkFrozen() {
		multiLayerNetworkFrozen.setParams(multiLayerNetworkTraining.params());
	}

	@Override
	public void train(List<Experience<S, A>> experiences, double discount) {

		List<ObservationSpace<S>> observationSpacesPrevious = new ArrayList<>();
		List<ObservationSpace<S>> observationSpacesNext = new ArrayList<>();

		for (Experience<S, A> experience : experiences) {
			observationSpacesPrevious.add(experience.getObservationSpacePrevious());
			observationSpacesNext.add(experience.getObservationSpaceNext());
		}

		INDArray arrayInputPrevious = createArrayFromObservedSpace(observationSpacesPrevious);
		INDArray arrayInputNext = createArrayFromObservedSpace(observationSpacesNext);

		INDArray arrayOutputPrevious = multiLayerNetworkTraining.output(arrayInputPrevious);
		INDArray arrayOutputNext = multiLayerNetworkTraining.output(arrayInputNext);

		INDArray arrayNext = Nd4j.argMax(arrayOutputNext, 1);

		for (int i = 0; i < experiences.size(); i++) {
			Experience<S, A> experience = experiences.get(i);

			if (arrayNext.getInt(i) == 0) {
				action1++;
			} else if (arrayNext.getInt(i) == 1) {
				action2++;
			} else if (arrayNext.getInt(i) == 2) {
				action3++;
			}

			double rewardCurrent;
			double rewardNext = arrayOutputNext.getDouble(i, arrayNext.getInt(i));
			double rewardCorrected;

			if (experience.getHappinessAfter() == 0) {
				rewardCurrent = -1;
			} else if (experience.getHappinessAfter() < experience.getHappinessBefore()) {
				rewardCurrent = 0;
			} else if (experience.getHappinessAfter() == experience.getHappinessBefore()) {
				rewardCurrent = 0.5;
			} else {
				rewardCurrent = 1;
			}

			if (rewardCurrent < 0) {
				rewardCorrected = rewardCurrent;
			} else {
				rewardCorrected = (rewardCurrent + discount * rewardNext) / (1 + discount);
			}

			arrayOutputPrevious.putScalar(i, experience.getAction().encode(), rewardCorrected);
		}

		multiLayerNetworkTraining.fit(arrayInputPrevious, arrayOutputPrevious);
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
		return Nd4j.create(space);
	}

	public static void save(NeuralNetworkPredictor<?, ?> predictor, String configuration, String parameters) {
		try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Paths.get(parameters)))) {
			Nd4j.write(predictor.multiLayerNetworkTraining.params(), dos);
			FileUtils.write(new File(configuration),
					predictor.multiLayerNetworkTraining.getLayerWiseConfigurations().toJson());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <S extends Enum<S> & IState, A extends Enum<A> & IAction> NeuralNetworkPredictor<S, A> load(
			Class<S> classState, Class<A> classAction, String configuration, String parameters) {

		try (DataInputStream dis = new DataInputStream(Files.newInputStream(Paths.get(parameters)))) {

			MultiLayerConfiguration multiLayerConfiguration = MultiLayerConfiguration
					.fromJson(FileUtils.readFileToString(new File(configuration)));

			NeuralNetworkPredictor<S, A> neuralNetworkPredictor = new NeuralNetworkPredictor<>(classState, classAction);
			neuralNetworkPredictor.multiLayerNetworkTraining = new MultiLayerNetwork(multiLayerConfiguration);
			neuralNetworkPredictor.multiLayerNetworkTraining.init();
			neuralNetworkPredictor.multiLayerNetworkTraining.setParameters(Nd4j.read(dis));
			neuralNetworkPredictor.multiLayerNetworkFrozen = new MultiLayerNetwork(multiLayerConfiguration);
			neuralNetworkPredictor.multiLayerNetworkFrozen.init();
			neuralNetworkPredictor.multiLayerNetworkFrozen
					.setParams(neuralNetworkPredictor.multiLayerNetworkTraining.params());

			return neuralNetworkPredictor;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
