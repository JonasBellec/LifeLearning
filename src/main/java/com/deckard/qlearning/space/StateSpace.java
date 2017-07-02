package com.deckard.qlearning.space;

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

	private StateSpace(Class<S> clazz) {
		super(clazz);
	}

	public List<IState> getUniverseStateSpace() {
		return null;
	}

	public List<IState> getAgentStateSpace() {
		return null;
	}
}
