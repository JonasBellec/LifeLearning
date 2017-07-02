package com.deckard.lifelearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.deckard.lifelearning.policy.IPolicy;
import com.deckard.qlearning.universe.IRealUniverse;

public class Environment implements IRealUniverse<State, Action> {

	private List<Agent> listAgent = new ArrayList<>();
	private IPolicy policy;

	private Integer day = 0;
	private Integer hour = 0;

	public Environment(IPolicy policy, Integer numberAgent) {
		this.policy = policy;

		for (int i = 0; i < numberAgent; i++) {
			listAgent.add(new Agent());
		}
	}

	public void tick() {

		hour++;
		if (hour >= 24) {
			day++;
			hour = 0;
		}

		for (Agent agent : listAgent) {
			agent.life();
			if (agent.getAlive()) {
				Action action = policy.predict(agent, populateMapVariable(agent));
				agent.act(action);
			}
		}
	}

	public Map<String, Double> populateMapVariable(Agent agent) {
		Map<String, Double> mapVariable = new HashMap<>();

		mapVariable.put("need1", (double) agent.getMapNeed().get(Need.NEED1));
		mapVariable.put("need2", (double) agent.getMapNeed().get(Need.NEED2));
		mapVariable.put("need3", (double) agent.getMapNeed().get(Need.NEED3));
		mapVariable.put("happiness", (double) agent.getHappiness());
		mapVariable.put("day", (double) day);
		mapVariable.put("hour", (double) hour);

		return mapVariable;
	}

	public List<Agent> getListAgent() {
		return listAgent;
	}

	public void setListAgent(List<Agent> listAgent) {
		this.listAgent = listAgent;
	}

	public IPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(IPolicy policy) {
		this.policy = policy;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}
}
