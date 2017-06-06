package com.deckard.lifelearning;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.RandomUtils;

public class Predictor {

	private Map<String, Double> mapParametre;

	public Double predict(Map<String, Double> mapVariable) {
		Double result = 0D;

		for (Entry<String, Double> entry : mapVariable.entrySet()) {
			Double parametre = mapParametre.get(entry.getKey());
			Double variable = entry.getValue();

			if (parametre == null) {
				parametre = RandomUtils.nextDouble();
				mapParametre.put(entry.getKey(), parametre);
			}

			if (variable == null) {
				variable = 0D;
			}

			result += parametre * variable;
		}

		return result;
	}
}
