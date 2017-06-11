package com.deckard.lifelearning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.deckard.lifelearning.policy.IPolicy;
import com.deckard.lifelearning.policy.TrainingPolicy;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class Training {
	public static void main(String[] args) {

		IPolicy policy = new TrainingPolicy();

		Environment environment = new Environment(policy, 100);

		for (int i = 0; i < 24; i++) {
			environment.tick();
		}

		Map<Action, Instances> mapFusionned = new HashMap<>();

		for (Agent agent : environment.getListAgent()) {
			Map<Action, Instances> mapInstances = ((TrainingPolicy) environment.getPolicy()).getData().get(agent);

			for (Entry<Action, Instances> entry : mapInstances.entrySet()) {
				Attribute reward = entry.getValue().attribute("reward");

				for (Instance instance : entry.getValue()) {
					instance.setValue(reward, agent.computeReward());
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
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
