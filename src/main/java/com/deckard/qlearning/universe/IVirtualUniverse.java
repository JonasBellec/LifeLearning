package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;

public interface IVirtualUniverse<S extends Enum<S> & IState<?>, A extends Enum<A> & IAction> extends IUniverse<S, A> {

	IAgent<S, A> getOwner();

	void step();

	void close();

	boolean isDone();
}
