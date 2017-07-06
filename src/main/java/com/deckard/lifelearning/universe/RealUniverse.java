package com.deckard.lifelearning.universe;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.deckard.lifelearning.model.Action;
import com.deckard.lifelearning.model.Agent;
import com.deckard.lifelearning.model.State;
import com.deckard.qlearning.policy.IPolicy;
import com.deckard.qlearning.space.Observation;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IRealUniverse;
import com.deckard.qlearning.universe.IVirtualUniverse;

public class RealUniverse implements IRealUniverse<State, Action> {

	private Time time;
	private List<Agent> agents;
	private IPolicy<State, Action> policy;

	private ObservationSpace<State> observationSpace;

	private Logger logger = Logger.getLogger(RealUniverse.class.getSimpleName());

	public RealUniverse(IPolicy<State, Action> policy, Integer numberAgent) {
		this.policy = policy;
		this.time = new Time();

		this.agents = new ArrayList<>();
		for (int i = 0; i < numberAgent; i++) {
			agents.add(new Agent());
		}

		observationSpace = new ObservationSpace<>();
	}

	@Override
	public ObservationSpace<State> getObservationSpace(List<State> states) {
		observationSpace.clear();
		observationSpace.add(new Observation<State, Integer>(State.DAY, time.getDay()));
		observationSpace.add(new Observation<State, Integer>(State.HOUR, time.getHour()));

		return observationSpace;
	}

	@Override
	public IVirtualUniverse<State, Action> virtualize(IAgent<State, Action> agent) {
		return new VirtualUniverse(this, agent);
	}

	@Override
	public void step() {
		time.tick();

		for (Agent agent : agents) {
			agent.life();
			if (agent.isAlive()) {
				Action action = policy.determineActionWithLearning(this, agent);
				agent.act(action);
				logger.log(Level.INFO, agent.toString() + " => " + action.toString());
			}
		}
	}

	/**
	 * @return the agents
	 */
	public List<Agent> getAgents() {
		return agents;
	}

	/**
	 * @return the time
	 */
	public Time getTime() {
		return time;
	}
}
