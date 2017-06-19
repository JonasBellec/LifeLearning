package com.deckard.lifelearning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.deckard.lifelearning.policy.IPolicy;
import com.deckard.lifelearning.policy.TrainingPolicy;

import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

public class Training {
	public static void main(String[] args) {

		IPolicy policy = new TrainingPolicy();

		Environment environment = new Environment(policy, 10000);

		Integer days = 2;

		for (int i = 0; i < 24 * days; i++) {
			environment.tick();
		}

		Map<Action, Instances> mapFusionned = new HashMap<>();

		for (Agent agent : environment.getListAgent()) {
			Map<Action, Instances> mapInstances = ((TrainingPolicy) environment.getPolicy()).getData().get(agent);

			for (Entry<Action, Instances> entry : mapInstances.entrySet()) {
				Attribute attribute = entry.getValue().attribute("reward");
				entry.getValue().setClass(attribute);

				for (Instance instance : entry.getValue()) {
					instance.setValue(attribute, agent.computeReward());
				}

				Instances instances = mapFusionned.get(entry.getKey());

				if (instances == null) {
					mapFusionned.put(entry.getKey(), entry.getValue());
				} else {
					instances.addAll(entry.getValue());
				}
			}
		}

		try {
			for (Entry<Action, Instances> entry : mapFusionned.entrySet()) {
				ArffLoader loader = new ArffLoader();
				loader.setFile(new File("" + entry.getKey().toString() + ".arff"));
				Instances instances = loader.getDataSet();

				instances.addAll(entry.getValue());

				Attribute attribute = instances.attribute("reward");
				instances.setClass(attribute);

				BufferedWriter writer = new BufferedWriter(new FileWriter("" + entry.getKey().toString() + ".arff"));

				writer.write(instances.toString());
				writer.newLine();
				writer.flush();
				writer.close();

				if (!instances.isEmpty()) {
					LinearRegression classifier = new LinearRegression();
					classifier.buildClassifier(instances);

					SerializationHelper.write("" + entry.getKey().toString() + ".model", classifier);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
