package com.deckard.qlearning.space;

import java.io.Serializable;

public interface IState<V extends Serializable> extends IEncodable {
	String getName();

	Class<V> getAssociatedValueClass();
}
