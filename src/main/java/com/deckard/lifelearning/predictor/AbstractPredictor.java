package com.deckard.lifelearning.predictor;

import weka.core.Instance;

public abstract class AbstractPredictor {
	public abstract Double predict(Instance instance);
}
