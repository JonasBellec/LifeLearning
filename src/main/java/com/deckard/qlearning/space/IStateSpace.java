package com.deckard.qlearning.space;

import java.util.List;

public interface IStateSpace extends Iterable<IState<?>> {
	int totalSize();

	List<IState<?>> getUniverseStateSpace();

	List<IState<?>> getAgentStateSpace();
}
