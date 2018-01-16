package com.deckard.qlearning.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.deckard.qlearning.predictor.Experience;
import com.deckard.qlearning.predictor.IPredictor;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IUniverse;

public class QLearningPolicy<S extends Enum<S> & IState, A extends Enum<A> & IAction> implements IPolicy<S, A> {

	private Random random;
	private IPredictor<S, A> predictor;
	private PolicyConfiguration<S, A> policyConfiguration;

	private List<Experience<S, A>> collectiveMemory;

	public QLearningPolicy(PolicyConfiguration<S, A> policyConfiguration, IPredictor<S, A> predictor) {
		this.policyConfiguration = policyConfiguration;
		this.predictor = predictor;

		this.random = new Random();
		this.collectiveMemory = new ArrayList<>();
	}

	@Override
	public A determineAction(IUniverse<S, A> universe, IAgent<S, A> agent) {
		Experience<S, A> experienceOld = agent.getLastExperience();

		ObservationSpace<S> observationSpaceCurrent = determineObservationSpace(universe, agent);
		Integer happinessCurrent = agent.computeHappiness();

		if (experienceOld != null) {
			experienceOld.setObservationSpaceNext(observationSpaceCurrent);
			experienceOld.setHappinessAfter(happinessCurrent);
			// on ajoute l'expérience dans la mémoire collective qu'une fois qu'elle ait été complétée
			collectiveMemory.add(experienceOld);
		}

		if (agent.isAlive()) {
			Experience<S, A> experienceNew = new Experience<>();
			experienceNew.setObservationSpacePrevious(observationSpaceCurrent);
			experienceNew.setHappinessBefore(happinessCurrent);

			if (random.nextDouble() > policyConfiguration.getEpsilon()) {
				experienceNew.setAction(predictor.predict(experienceNew.getObservationSpacePrevious()));
			} else {
				experienceNew.setAction(policyConfiguration.getActionSpace().random());
			}

			agent.addToMemory(experienceNew);
			return experienceNew.getAction();
		} else {
			// L'agent est mort, il n'agit donc plus
			return null;
		}
	}

	private ObservationSpace<S> determineObservationSpace(IUniverse<S, A> universe, IAgent<S, A> agent) {
		ObservationSpace<S> observationSpace = new ObservationSpace<>();

		observationSpace.addAll(universe.getObservationSpace(policyConfiguration.getStateSpace().getUniverseStates()));
		observationSpace.addAll(agent.getObservationSpace(policyConfiguration.getStateSpace().getAgentStates()));

		return observationSpace;
	}

	@Override
	public void learnFromCollectiveMemory() {
		List<Experience<S, A>> sample = new ArrayList<>();
		List<Experience<S, A>> experiencesToRemove = new ArrayList<>();

		if (collectiveMemory.size() > 1000) {
			for (Experience<S, A> experience : collectiveMemory) {
				Double probability = ((double) (experience.getProbability() * 1000))
						/ ((double) (collectiveMemory.size()));

				if (random.nextDouble() < probability) {
					sample.add(experience);
				}

				if (random.nextDouble() < Math.tanh(collectiveMemory.size()) / 10) {
					experiencesToRemove.add(experience);
				}
			}

			if (!sample.isEmpty()) {
				predictor.train(sample, 0.8);
			}

			if (random.nextDouble() < 0.1) {
				predictor.updateMultiLayerNetworkFrozen();
			}

			collectiveMemory.removeAll(experiencesToRemove);
		}
	}
}
