package com.deckard.lifelearning.predictor;

import java.io.FileInputStream;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.SerializationHelper;

public class ClassifierPredictor extends AbstractPredictor {

	private Classifier cls;

	public ClassifierPredictor(String fileModel) {
		try {
			cls = (Classifier) SerializationHelper.read(new FileInputStream(fileModel + ".model"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Double predict(Instance instance) {
		try {
			return cls.classifyInstance(instance);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
