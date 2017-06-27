package com.deckard.qlearning.mdp;

import com.deckard.qlearning.space.IState;

public interface IVirtualUnivers<S extends IState> extends IUnivers<S> {

	void close();

	boolean isDone();
}
