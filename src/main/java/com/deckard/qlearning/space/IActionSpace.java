package com.deckard.qlearning.space;

public interface IActionSpace extends Iterable<IAction> {

	int size();

	IAction random();

	IAction noOp();

	IAction decode(int code);
}
