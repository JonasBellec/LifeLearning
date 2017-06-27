package com.deckard.qlearning.space;

public interface IActionSpace<A extends IAction> extends Iterable<A> {

	int size();

	A random();

	A noOp();
}
