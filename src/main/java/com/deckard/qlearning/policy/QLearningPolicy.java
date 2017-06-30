package com.deckard.qlearning.policy;

import java.util.Random;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IStateSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IRealUniverse;
import com.deckard.qlearning.universe.IVirtualUniverse;

public class QLearningPolicy<T extends IStateSpace, B extends IActionSpace> implements IPolicy<T, B> {

	private IPredictor<T, B> predictor;
	private Random random;

	public double getEpsilon() {
		return 0.2d;
	}

	@Override
	public IAction determineActionWithLearning(IRealUniverse<T> realUniverse, IAgent<T, B> agent) {
		if (random.nextDouble() > getEpsilon()) {
			return predictor.predictAction(realUniverse.virtualize(agent), agent);
		} else {
			return agent.getActionSpace().random();
		}
	}

	@Override
	public IAction determineActionWithoutLearning(IRealUniverse<T> realUniverse, IAgent<T, B> agent) {
		return determineAction(realUniverse.virtualize(agent), agent);
	}

	public IAction determineAction(IVirtualUniverse<T> virtualUniverse, IAgent<T, B> agent) {
		if (random.nextDouble() > getEpsilon()) {
			return predictor.predictAction(virtualUniverse, agent);
		} else {
			return agent.getActionSpace().random();
		}
	}
}
