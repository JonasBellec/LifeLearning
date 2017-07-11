package com.deckard.lifelearning.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;

import com.deckard.qlearning.space.Observation;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.universe.IAgent;

public class Agent implements IAgent<State, Action> {

	private Map<Need, Integer> mapNeed;
	private Integer ticks;
	private boolean alive;

	private ObservationSpace<State> observationSpace;

	public Agent() {
		this.mapNeed = new HashMap<>();
		this.mapNeed.put(Need.NEED1, RandomUtils.nextInt() % 10 + 5);
		this.mapNeed.put(Need.NEED2, RandomUtils.nextInt() % 10 + 5);
		this.mapNeed.put(Need.NEED3, RandomUtils.nextInt() % 10 + 5);

		this.ticks = 0;
		this.alive = true;

		observationSpace = new ObservationSpace<>();
	}

	public Agent(Agent agent) {
		this.mapNeed = new HashMap<>();
		this.mapNeed.put(Need.NEED1, agent.mapNeed.get(Need.NEED1));
		this.mapNeed.put(Need.NEED2, agent.mapNeed.get(Need.NEED2));
		this.mapNeed.put(Need.NEED3, agent.mapNeed.get(Need.NEED3));

		this.ticks = agent.ticks;
		this.alive = agent.alive;

		observationSpace = new ObservationSpace<>();
	}

	@Override
	public Agent virtualize() {
		return new Agent(this);
	}

	@Override
	public ObservationSpace<State> getObservationSpace(List<State> states) {
		observationSpace.clear();
		observationSpace.add(new Observation<State, Integer>(State.NEED1, mapNeed.get(Need.NEED1)));
		observationSpace.add(new Observation<State, Integer>(State.NEED2, mapNeed.get(Need.NEED2)));
		observationSpace.add(new Observation<State, Integer>(State.NEED3, mapNeed.get(Need.NEED3)));

		return observationSpace;
	}

	@Override
	public void life() {
		ticks++;

		if (alive) {
			for (Need need : mapNeed.keySet()) {
				Integer oldScore = mapNeed.get(need);
				mapNeed.put(need, --oldScore);
			}

			for (Integer needScore : mapNeed.values()) {
				if (needScore <= 0) {
					alive = false;
				}
			}
		}
	}

	@Override
	public void act(Action action) {
		Integer oldScore = mapNeed.get(action.getNeed());

		if (oldScore < 10) {
			mapNeed.put(action.getNeed(), oldScore + 4);
		}
	}

	@Override
	public int computeReward() {
		if (alive) {
			return (mapNeed.get(Need.NEED1) + mapNeed.get(Need.NEED2) + mapNeed.get(Need.NEED3));
		} else {
			return 0;
		}
	}

	/**
	 * @return the alive
	 */
	@Override
	public boolean isAlive() {
		return alive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Agent [mapNeed=" + mapNeed + ", ticks=" + ticks + ", alive=" + alive;
	}
}
