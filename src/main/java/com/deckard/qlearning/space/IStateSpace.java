package com.deckard.qlearning.space;

import java.util.ArrayList;
import java.util.List;

public abstract class IStateSpace extends ArrayList<IState<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1763039952633380076L;

	private IStateSpace() {

	}

	public static IStateSpace getInstance() {
		return null;
	}

	public abstract int totalSize();

	public abstract List<IState<?>> getUniverseStateSpace();

	public abstract List<IState<?>> getAgentStateSpace();
}
