package com.deckard.qlearning.space;

import java.util.HashMap;
import java.util.Map;

public class ActionSpace<A extends Enum<A> & IAction> extends Space<A> {

	private static Map<Class<?>, ActionSpace<?>> mapInstance = new HashMap<>();

	public static <A extends Enum<A> & IAction> ActionSpace<A> getInstance(Class<A> clazz) {

		@SuppressWarnings("unchecked")
		ActionSpace<A> instance = (ActionSpace<A>) mapInstance.get(clazz);

		if (instance == null && clazz.isEnum()) {
			instance = new ActionSpace<>(clazz);
			mapInstance.put(clazz, instance);
		}

		return instance;
	}

	private ActionSpace(Class<A> clazz) {
		super(clazz);
	}
}
