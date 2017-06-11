package com.deckard.lifelearning.policy;

import java.util.Map;

import com.deckard.lifelearning.Action;
import com.deckard.lifelearning.Agent;

public interface IPolicy {
	public Action predict(Agent agent, Map<String, Double> mapVariable);
}
