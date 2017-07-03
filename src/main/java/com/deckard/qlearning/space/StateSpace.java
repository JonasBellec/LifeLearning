package com.deckard.qlearning.space;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateSpace<S extends Enum<S> & IState> extends Space<S> {

	private static Map<Class<?>, StateSpace<?>> mapInstance = new HashMap<>();

	public static <S extends Enum<S> & IState> StateSpace<S> getInstance(Class<S> clazz) {

		@SuppressWarnings("unchecked")
		StateSpace<S> instance = (StateSpace<S>) mapInstance.get(clazz);

		if (instance == null && clazz.isEnum()) {
			instance = new StateSpace<>(clazz);
			mapInstance.put(clazz, instance);
		}

		return instance;
	}

	private List<S> universeStateSpace = null;
	private List<S> agentStateSpace = null;

	private StateSpace(Class<S> clazz) {
		super(clazz);
	}

	public List<S> getUniverseStates() {
		if (universeStateSpace == null) {
			universeStateSpace = new ArrayList<>();

			for (S state : clazz.getEnumConstants()) {
				if (state.isUniverseState()) {
					universeStateSpace.add(state);
				}
			}
		}

		return universeStateSpace;
	}

	public List<S> getAgentStates() {
		if (agentStateSpace == null) {
			agentStateSpace = new ArrayList<>();

			for (S state : clazz.getEnumConstants()) {
				if (state.isAgentState()) {
					agentStateSpace.add(state);
				}
			}
		}

		return agentStateSpace;
	}
}
