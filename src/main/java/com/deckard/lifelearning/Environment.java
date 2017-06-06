package com.deckard.lifelearning;

import java.util.List;

public class Environment {

	private List<Agent> listAgent;

	public void tick() {
		for (Agent agent : listAgent) {
			agent.life();
		}
	}
}
