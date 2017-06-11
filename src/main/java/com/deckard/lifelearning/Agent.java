package com.deckard.lifelearning;

import java.util.HashMap;
import java.util.Map;

public class Agent {

	private Map<Need, Integer> mapNeed;
	private Integer happiness = 0;
	private Boolean alive = true;

	public Agent() {
		mapNeed = new HashMap<>();
		mapNeed.put(Need.NEED1, 10);
		mapNeed.put(Need.NEED2, 10);
		mapNeed.put(Need.NEED3, 10);
	}

	public void life() {
		for (Need need : mapNeed.keySet()) {
			Integer oldScore = mapNeed.get(need);
			mapNeed.put(need, --oldScore);
		}
	}

	public void act(Action action) {
		if (action == Action.ACTION4) {
			happiness++;
		} else {
			Integer oldScore = mapNeed.get(action.getNeed());
			mapNeed.put(action.getNeed(), oldScore + 4);
		}
	}

	public Integer computeReward() {
		for (Integer needScore : mapNeed.values()) {
			if (needScore <= 0) {
				alive = false;
				return -1000;
			}
		}

		return happiness;
	}

	public Map<Need, Integer> getMapNeed() {
		return mapNeed;
	}

	public void setMapNeed(Map<Need, Integer> mapNeed) {
		this.mapNeed = mapNeed;
	}

	public Integer getHappiness() {
		return happiness;
	}

	public void setHappiness(Integer happiness) {
		this.happiness = happiness;
	}

	public Boolean getAlive() {
		return alive;
	}

	public void setAlive(Boolean alive) {
		this.alive = alive;
	}
}
