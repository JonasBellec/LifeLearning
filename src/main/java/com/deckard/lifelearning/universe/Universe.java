package com.deckard.lifelearning.universe;

import java.util.ArrayList;
import java.util.List;

import com.deckard.lifelearning.model.Action;
import com.deckard.lifelearning.model.Agent;
import com.deckard.lifelearning.model.State;
import com.deckard.qlearning.policy.IPolicy;
import com.deckard.qlearning.space.Observation;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.universe.IUniverse;

public class Universe implements IUniverse<State, Action> {

	private Time time;
	private List<Agent> agents;
	private IPolicy<State, Action> policy;

	private ObservationSpace<State> observationSpace;

	int action1 = 0;
	int action2 = 0;
	int action3 = 0;

	public Universe(IPolicy<State, Action> policy, Integer numberAgent) {
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
	public void step() {
		time.tick();

		for (Agent agent : agents) {
			if (agent.isAlive()) {
				agent.life();
				Action action = policy.determineAction(this, agent);
				if (action != null) {
					if (action == Action.ACTION1) {
						action1++;
					} else if (action == Action.ACTION2) {
						action2++;
					} else if (action == Action.ACTION3) {
						action3++;
					}

					agent.act(action);
				}
			}
		}

		policy.learnFromCollectiveMemory();
		System.out.println(String.format("action1 : %d, action2 : %d, action3 : %d", action1, action2, action3));
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
