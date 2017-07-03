package com.deckard.qlearning.policy;

import java.util.Random;

import com.deckard.qlearning.space.ActionSpace;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.space.StateSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IRealUniverse;
import com.deckard.qlearning.universe.IVirtualUniverse;

public class QLearningPolicy<S extends Enum<S> & IState, A extends Enum<A> & IAction> implements IPolicy<S, A> {

	private IPredictor<S, A> predictor;
	private Random random;
	private StateSpace<S> stateSpace;
	private ActionSpace<A> actionSpace;

	public QLearningPolicy(Class<S> classState, Class<A> classAction, IPredictor<S, A> predictor) {
		this.stateSpace = StateSpace.getInstance(classState);
		this.actionSpace = ActionSpace.getInstance(classAction);
		this.predictor = predictor;
	}

	public double getEpsilon() {
		return 0.2d;
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
		if (random.nextDouble() > getEpsilon()) {
			return predictor.predictAction(determineObservationSpace(virtualUniverse, agent));
		} else {
			return actionSpace.random();
		}
	}

	private ObservationSpace<S> determineObservationSpace(IVirtualUniverse<S, A> virtualUniverse, IAgent<S, A> agent) {
		ObservationSpace<S> observationSpace = new ObservationSpace<>();

		observationSpace.addAll(virtualUniverse.getObservationSpace(stateSpace.getUniverseStates()));
		observationSpace.addAll(agent.getObservationSpace(stateSpace.getAgentStates()));

		return observationSpace;
	}
}
