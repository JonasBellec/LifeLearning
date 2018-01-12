package com.deckard.lifelearning.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;

import com.deckard.qlearning.predictor.Experience;
import com.deckard.qlearning.space.Observation;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.universe.IAgent;

public class Agent implements IAgent<State, Action> {

	private Map<Need, Integer> mapNeed;
	private boolean alive;

	private ObservationSpace<State> observationSpace;

	private List<Experience<State, Action>> memory;

	public Agent() {
		this.mapNeed = new HashMap<>();
		this.mapNeed.put(Need.NEED1, RandomUtils.nextInt() % 10 + 5);
		this.mapNeed.put(Need.NEED2, RandomUtils.nextInt() % 10 + 5);
		this.mapNeed.put(Need.NEED3, RandomUtils.nextInt() % 10 + 5);

		this.alive = true;

		observationSpace = new ObservationSpace<>();
		memory = new ArrayList<>();
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
	public Integer computeHappiness() {
		if (alive) {
			return mapNeed.get(Need.NEED1) + mapNeed.get(Need.NEED2) + mapNeed.get(Need.NEED3);
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

	@Override
	public void addToMemory(Experience<State, Action> experience) {
		memory.add(experience);
	}

	@Override
	public Experience<State, Action> getLastExperience() {
		if (!memory.isEmpty()) {
			return memory.get(memory.size() - 1);
		} else {
			return null;
		}
	}
}
