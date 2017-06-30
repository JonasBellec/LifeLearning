package com.deckard.qlearning.deeplearning;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.Learning.InitMdp;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning.QLStatEntry;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning.QLStepReturn;
import org.deeplearning4j.rl4j.util.DataManager.StatEntry;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.deckard.qlearning.policy.IPredictor;
import com.deckard.qlearning.policy.Transition;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IObservationSpace;
import com.deckard.qlearning.space.IStateSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IVirtualUniverse;

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
	public IAction predictAction(IVirtualUniverse<T> virtualUniverse, IAgent<T, B> agent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void train(List<Transition<T>> transitions) {

		INDArray arrayInputSource = createArrayFromObservedSpace(transitions.stream().map(p -> p.getObservationSpaceSource()));
		INDArray arrayInputTarget = createArrayFromObservedSpace(transitions.stream().map(p -> p.getObservationSpaceTarget()));

		INDArray arrayOutputSource = multiLayerNetworkSource.output(arrayInputSource);
		INDArray arrayOutputTarget = multiLayerNetworkTarget.output(arrayInputTarget);

		double error = 0;

		for (int i = 0; i < transitions.size(); i++) {
			int ind[] = { i, replays[i].Action };
			float FutureReward = 0;
			if (replays[i].NextInput != null)
				FutureReward = FindMax(TargetOutputs.getRow(i), replays[i].NextActionMask);

			double rewardTarget = replays[i].Reward + discount * FutureReward;

			error += (rewardTarget - arrayOutputSource.getFloat(ind)) * (rewardTarget - arrayOutputSource.getFloat(ind));

			arrayOutputSource.putScalar(ind, rewardTarget);
		}
		// System.out.println("Average Error: " + (error / transitions.size()) );

		multiLayerNetworkSource.fit(arrayInputSource, arrayOutputSource);
	}

	private INDArray createArrayFromObservedSpace(Stream<IObservationSpace<T>> observationSpaces) {
		INDArray retVal = Nd4j.create(replays.length, InputLength);
		for (int i = 0; i < replays.length; i++) {
			retVal.putRow(i, replays[i].Input);
		}
		return retVal;
	}

	protected StatEntry trainEpoch() {
		InitMdp<O> initMdp = initMdp();
		O obs = initMdp.getLastObs();

		double reward = initMdp.getReward();
		int step = initMdp.getSteps();

		Double startQ = Double.NaN;
		double meanQ = 0;
		int numQ = 0;
		List<Double> scores = new ArrayList<>();
		while (step < getConfiguration().getMaxEpochStep() && !getMdp().isDone()) {

			if (getStepCounter() % getConfiguration().getTargetDqnUpdateFreq() == 0) {
				updateTargetNetwork();
			}

			QLStepReturn<O> stepR = trainStep(obs);

			if (!stepR.getMaxQ().isNaN()) {
				if (startQ.isNaN())
					startQ = stepR.getMaxQ();
				numQ++;
				meanQ += stepR.getMaxQ();
			}

			if (stepR.getScore() != 0)
				scores.add(stepR.getScore());

			reward += stepR.getStepReply().getReward();
			obs = stepR.getStepReply().getObservation();
			incrementStep();
			step++;
		}

		meanQ /= (numQ + 0.001); // avoid div zero

		StatEntry statEntry = new QLStatEntry(getStepCounter(), getEpochCounter(), reward, step, scores, getEgPolicy().getEpsilon(), startQ, meanQ);

		return statEntry;

	}

	protected QLStepReturn<O> trainStep(O obs) {

		Integer action;
		INDArray input = getInput(obs);
		boolean isHistoryProcessor = getHistoryProcessor() != null;

		if (isHistoryProcessor)
			getHistoryProcessor().record(input);

		int skipFrame = isHistoryProcessor ? getHistoryProcessor().getConf().getSkipFrame() : 1;
		int historyLength = isHistoryProcessor ? getHistoryProcessor().getConf().getHistoryLength() : 1;
		int updateStart = getConfiguration().getUpdateStart() + ((getConfiguration().getBatchSize() + historyLength) * skipFrame);

		Double maxQ = Double.NaN; // ignore if Nan for stats

		// if step of training, just repeat lastAction
		if (getStepCounter() % skipFrame != 0) {
			action = lastAction;
		} else {
			if (history == null) {
				if (isHistoryProcessor) {
					getHistoryProcessor().add(input);
					history = getHistoryProcessor().getHistory();
				} else
					history = new INDArray[] { input };
			}
			// concat the history into a single INDArray input
			INDArray hstack = Transition.concat(Transition.dup(history));

			// if input is not 2d, you have to append that the batch is 1 length high
			if (hstack.shape().length > 2)
				hstack = hstack.reshape(Learning.makeShape(1, hstack.shape()));

			INDArray qs = getCurrentDQN().output(hstack);
			int maxAction = Learning.getMaxAction(qs);

			maxQ = qs.getDouble(maxAction);
			action = getEgPolicy().nextAction(hstack);
		}

		lastAction = action;

		StepReply<O> stepReply = getMdp().step(action);

		accuReward += stepReply.getReward() * configuration.getRewardFactor();

		// if it's not a skipped frame, you can do a step of training
		if (getStepCounter() % skipFrame == 0 || stepReply.isDone()) {

			INDArray ninput = getInput(stepReply.getObservation());
			if (isHistoryProcessor)
				getHistoryProcessor().add(ninput);

			INDArray[] nhistory = isHistoryProcessor ? getHistoryProcessor().getHistory() : new INDArray[] { ninput };

			Transition<Integer> trans = new Transition(history, action, accuReward, stepReply.isDone(), nhistory[0]);
			getExpReplay().store(trans);

			if (getStepCounter() > updateStart) {
				Pair<INDArray, INDArray> targets = setTarget(getExpReplay().getBatch());
				getCurrentDQN().fit(targets.getFirst(), targets.getSecond());
			}

			history = nhistory;
			accuReward = 0;
		}

		return new QLStepReturn<O>(maxQ, getCurrentDQN().getLatestScore(), stepReply);

	}

	protected Pair<INDArray, INDArray> setTarget(List<Transition<T>> transitions) {

		int size = transitions.size();

		int[] shape = getHistoryProcessor() == null ? getMdp().getObservationSpace().getShape() : getHistoryProcessor().getConf().getShape();
		int[] nshape = makeShape(size, shape);
		INDArray obs = Nd4j.create(nshape);
		INDArray nextObs = Nd4j.create(nshape);

		INDArray arrayInputSource = createArrayFromObservedSpace(transitions.stream().map(p -> p.getObservationSpaceSource()));
		INDArray arrayInputTarget = createArrayFromObservedSpace(transitions.stream().map(p -> p.getObservationSpaceTarget()));

		int[] actions = new int[size];
		boolean[] areTerminal = new boolean[size];

		for (int i = 0; i < size; i++) {
			Transition<Integer> trans = transitions.get(i);
			areTerminal[i] = trans.isTerminal();
			actions[i] = trans.getAction();
			obs.putRow(i, Transition.concat(trans.getObservation()));
			nextObs.putRow(i, Transition.concat(Transition.append(trans.getObservation(), trans.getNextObservation())));
		}

		INDArray dqnOutputAr = dqnOutput(obs);

		INDArray dqnOutputNext = dqnOutput(nextObs);
		INDArray targetDqnOutputNext = null;

		INDArray tempQ = null;
		INDArray getMaxAction = null;
		if (getConfiguration().isDoubleDQN()) {
			targetDqnOutputNext = targetDqnOutput(nextObs);
			getMaxAction = Nd4j.argMax(dqnOutputNext, 1);
		} else {
			tempQ = Nd4j.max(dqnOutputNext, 1);
		}

		for (int i = 0; i < size; i++) {
			double yTar = transitions.get(i).getReward();
			if (!areTerminal[i]) {
				double q = 0;
				if (getConfiguration().isDoubleDQN()) {
					q += targetDqnOutputNext.getDouble(i, getMaxAction.getInt(i));
				} else
					q += tempQ.getDouble(i);

				yTar += getConfiguration().getGamma() * q;

			}

			double previousV = dqnOutputAr.getDouble(i, actions[i]);
			double lowB = previousV - getConfiguration().getErrorClamp();
			double highB = previousV + getConfiguration().getErrorClamp();
			double clamped = Math.min(highB, Math.max(yTar, lowB));

			dqnOutputAr.putScalar(i, actions[i], clamped);
		}

		return new Pair(obs, dqnOutputAr);
	}

}
