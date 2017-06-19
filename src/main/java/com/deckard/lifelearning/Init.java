package com.deckard.lifelearning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.deckard.lifelearning.policy.IPolicy;
import com.deckard.lifelearning.policy.InitPolicy;

import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class Init {
	public static void main(String[] args) {

		IPolicy policy = new InitPolicy();

		Environment environment = new Environment(policy, 10000);

		Integer days = 2;

		for (int i = 0; i < 24 * days; i++) {
			environment.tick();
		}

		Map<Action, Instances> mapFusionned = new HashMap<>();

		for (Agent agent : environment.getListAgent()) {
			Map<Action, Instances> mapInstances = ((InitPolicy) environment.getPolicy()).getData().get(agent);

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
				BufferedWriter writer = new BufferedWriter(new FileWriter("" + entry.getKey().toString() + ".arff"));

				writer.write(entry.getValue().toString());
				writer.newLine();
				writer.flush();
				writer.close();

				if (!entry.getValue().isEmpty()) {
					LinearRegression classifier = new LinearRegression();
					classifier.buildClassifier(entry.getValue());

					SerializationHelper.write("" + entry.getKey().toString() + ".model", classifier);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
