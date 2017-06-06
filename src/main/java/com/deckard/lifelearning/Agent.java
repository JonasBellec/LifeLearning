package com.deckard.lifelearning;

import java.util.Map;

public class Agent {

	private Map<Need, Integer> mapNeed;
	private Integer happiness;

	public void life() {
		for (Need need : mapNeed.keySet()) {
			Integer oldScore = mapNeed.get(need);
			mapNeed.put(need, oldScore--);
		}
	}

	public void act(Action action) {
		Integer oldScore = mapNeed.get(action.getNeed());
		mapNeed.put(action.getNeed(), oldScore + 4);
	}

	public Integer computeReward() {
		for (Integer needScore : mapNeed.values()) {
			if (needScore <= 0) {
				return -1000;
			}
		}

		return happiness;
	}
}
