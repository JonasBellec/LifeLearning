package com.deckard.lifelearning;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;

public class Agent {

	private Map<Need, Integer> mapNeed;
	private Integer happiness = 0;
	private Boolean alive = true;

	public Agent() {
		mapNeed = new HashMap<>();
		mapNeed.put(Need.NEED1, RandomUtils.nextInt() % 10 + 5);
		mapNeed.put(Need.NEED2, RandomUtils.nextInt() % 10 + 5);
		mapNeed.put(Need.NEED3, RandomUtils.nextInt() % 10 + 5);
	}

	public void life() {
		for (Need need : mapNeed.keySet()) {
			Integer oldScore = mapNeed.get(need);
			mapNeed.put(need, --oldScore);
		}
	}

	public void act(Action action) {
		if (action == Action.ACTION4) {
			happiness += 10;
		} else {
			Integer oldScore = mapNeed.get(action.getNeed());
			mapNeed.put(action.getNeed(), oldScore + 4);
		}
	}

	public Integer computeReward() {
		for (Integer needScore : mapNeed.values()) {
			if (needScore <= 0) {
				alive = false;
				return -10;
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
