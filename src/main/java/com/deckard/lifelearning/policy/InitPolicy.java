package com.deckard.lifelearning.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;

import com.deckard.lifelearning.Action;
import com.deckard.lifelearning.Agent;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class InitPolicy implements IPolicy {

	private List<String> listVariable = new ArrayList<>();
	private Map<Agent, Map<Action, Instances>> data = new HashMap<>();

	public InitPolicy() {
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

		Map<Action, Instances> mapInstances = data.get(agent);
		if (mapInstances == null) {
			mapInstances = createMapInstances(agent);
			data.put(agent, mapInstances);
		}

		Action action = randomAction();
		mapInstances.get(action).add(createInstance(mapVariable));
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

		return new DenseInstance(1, array);
	}

	private Map<Action, Instances> createMapInstances(Agent agent) {
		ArrayList<Attribute> listAttribute = new ArrayList<>();
		for (String variable : listVariable) {
			listAttribute.add(new Attribute(variable));
		}

		Map<Action, Instances> mapInstances = new HashMap<>();

		mapInstances.put(Action.ACTION1, new Instances("action1", listAttribute, 0));
		mapInstances.put(Action.ACTION2, new Instances("action2", listAttribute, 0));
		mapInstances.put(Action.ACTION3, new Instances("action3", listAttribute, 0));
		mapInstances.put(Action.ACTION4, new Instances("action4", listAttribute, 0));

		return mapInstances;
	}

	private Action randomAction() {
		return Action.ARRAY_ACTION[RandomUtils.nextInt() % Action.ARRAY_ACTION.length];
	}

	public List<String> getListVariable() {
		return listVariable;
	}

	public void setListVariable(List<String> listVariable) {
		this.listVariable = listVariable;
	}

	public Map<Agent, Map<Action, Instances>> getData() {
		return data;
	}

	public void setData(Map<Agent, Map<Action, Instances>> data) {
		this.data = data;
	}
}
