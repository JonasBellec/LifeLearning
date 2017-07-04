package com.deckard.qlearning.policy;

import java.util.Random;

import com.deckard.qlearning.predictor.IPredictor;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IRealUniverse;
import com.deckard.qlearning.universe.IVirtualUniverse;

public class QLearningPolicy<S extends Enum<S> & IState, A extends Enum<A> & IAction> implements IPolicy<S, A> {

	private IPredictor<S, A> predictor;
	private Random random;
	private PolicyConfiguration<S, A> policyConfiguration;

	public QLearningPolicy(PolicyConfiguration<S, A> policyConfiguration, IPredictor<S, A> predictor) {
		this.policyConfiguration = policyConfiguration;
		this.predictor = predictor;

		this.random = new Random();
	}

	@Override
	public A determineActionWithLearning(IRealUniverse<S, A> realUniverse, IAgent<S, A> agent) {
		return determineActionWithoutLearning(realUniverse, agent);
	}

	@Override
	public A determineActionWithoutLearning(IRealUniverse<S, A> realUniverse, IAgent<S, A> agent) {
		return determineAction(realUniverse.virtualize(agent), agent);
	}

	public A determineAction(IVirtualUniverse<S, A> virtualUniverse, IAgent<S, A> agent) {
		if (random.nextDouble() > policyConfiguration.getEpsilon()) {
			return predictor.predictAction(determineObservationSpace(virtualUniverse, agent));
		} else {
			return policyConfiguration.getActionSpace().random();
		}
	}

	private ObservationSpace<S> determineObservationSpace(IVirtualUniverse<S, A> virtualUniverse, IAgent<S, A> agent) {
		ObservationSpace<S> observationSpace = new ObservationSpace<>();

		observationSpace
				.addAll(virtualUniverse.getObservationSpace(policyConfiguration.getStateSpace().getUniverseStates()));
		observationSpace.addAll(agent.getObservationSpace(policyConfiguration.getStateSpace().getAgentStates()));

		return observationSpace;
	}
}
