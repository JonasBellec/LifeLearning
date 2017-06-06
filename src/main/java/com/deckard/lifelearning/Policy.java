package com.deckard.lifelearning;

import java.util.Map;
import java.util.Map.Entry;

public class Policy {

	private Map<Action, Predictor> mapPredictor;

	public Action predict(Map<String, Double> mapVariable) {
		Action action = null;
		Double maxResult = null;

		for (Entry<Action, Predictor> entry : mapPredictor.entrySet()) {
			Double result = entry.getValue().predict(mapVariable);

			if (maxResult == null || result > maxResult) {
				maxResult = result;
				action = entry.getKey();
			}
		}

		return action;
	}
}
