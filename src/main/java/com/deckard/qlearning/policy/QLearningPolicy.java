package com.deckard.qlearning.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.deckard.qlearning.predictor.IPredictor;
import com.deckard.qlearning.predictor.Transition;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IRealUniverse;
import com.deckard.qlearning.universe.IUniverse;
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

		IVirtualUniverse<S, A> virtualUniverse = realUniverse.virtualize(agent);
		List<Transition<S, A>> transitions = projectVirtualUniverse(virtualUniverse);
		predictor.train(transitions);

		return determineAction(realUniverse, agent);
	}

	@Override
	public A determineActionWithoutLearning(IRealUniverse<S, A> realUniverse, IAgent<S, A> agent) {
		return determineAction(realUniverse, agent);
	}

	private A determineAction(IUniverse<S, A> universe, IAgent<S, A> agent) {
		if (random.nextDouble() > policyConfiguration.getEpsilon()) {
			return predictor.predictAction(determineObservationSpace(universe, agent));
		} else {
			return policyConfiguration.getActionSpace().random();
		}
	}

	private ObservationSpace<S> determineObservationSpace(IUniverse<S, A> universe, IAgent<S, A> agent) {
		ObservationSpace<S> observationSpace = new ObservationSpace<>();

		observationSpace.addAll(universe.getObservationSpace(policyConfiguration.getStateSpace().getUniverseStates()));
		observationSpace.addAll(agent.getObservationSpace(policyConfiguration.getStateSpace().getAgentStates()));

		return observationSpace;
	}

	private List<Transition<S, A>> projectVirtualUniverse(IVirtualUniverse<S, A> virtualUniverse) {

		List<Transition<S, A>> transitions = new ArrayList<>();

		while (transitions.size() < policyConfiguration.getProjectionDeep()
				&& virtualUniverse.getVirtualOwner().isAlive()) {
			Transition<S, A> transition = new Transition<>();

			ObservationSpace<S> observationSpaceSource = determineObservationSpace(virtualUniverse,
					virtualUniverse.getVirtualOwner());

			transition.setObservationSpaceSource(observationSpaceSource);
			A action = predictor.predictAction(observationSpaceSource);
			transition.setAction(action);
			virtualUniverse.getVirtualOwner().act(action);

			virtualUniverse.step();

			transition.setReward(virtualUniverse.getVirtualOwner().computeReward());
			transition.setObservationSpaceTarget(
					determineObservationSpace(virtualUniverse, virtualUniverse.getVirtualOwner()));

			transitions.add(transition);
		}

		return transitions;
	}
}
