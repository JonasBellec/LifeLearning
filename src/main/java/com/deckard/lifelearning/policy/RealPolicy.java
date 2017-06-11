package com.deckard.lifelearning.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.deckard.lifelearning.Action;
import com.deckard.lifelearning.Agent;
import com.deckard.lifelearning.predictor.ClassifierPredictor;

import weka.core.DenseInstance;
import weka.core.Instance;

public class RealPolicy implements IPolicy {
	private List<String> listVariable = new ArrayList<>();
	private Map<Action, ClassifierPredictor> mapPredictor;

	public RealPolicy() {
		mapPredictor.put(Action.ACTION1, new ClassifierPredictor("action1"));
		mapPredictor.put(Action.ACTION2, new ClassifierPredictor("action2"));
		mapPredictor.put(Action.ACTION3, new ClassifierPredictor("action3"));
		mapPredictor.put(Action.ACTION4, new ClassifierPredictor("action4"));

		listVariable.add("need1");
		listVariable.add("need2");
		listVariable.add("need3");
		listVariable.add("happiness");
		listVariable.add("day");
		listVariable.add("hour");
		listVariable.add("reward");
	}

	@Override
	public Action predict(Agent agent, Map<String, Double> mapVariable) {
		Action action = null;
		Double maxResult = null;

		for (Entry<Action, ClassifierPredictor> entry : mapPredictor.entrySet()) {
			Double result = entry.getValue().predict(createInstance(mapVariable));

			if (maxResult == null || result > maxResult) {
				maxResult = result;
				action = entry.getKey();
			}
		}

		return action;
	}

	private Instance createInstance(Map<String, Double> mapVariable) {
		double[] array = new double[listVariable.size()];

		for (int i = 0; i < listVariable.size(); i++) {
			Double value = mapVariable.get(listVariable.get(i));

			if (value != null) {
				array[i] = value;
			} else {
				array[i] = 0;
			}
		}

		return new DenseInstance(0, array);
	}
}
