package com.deckard.qlearning.space;

import java.util.ArrayList;

public class ObservationSpace<S extends IState> extends ArrayList<Observation<S, ?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7043303084059810213L;

	public double[] encode() {

		double[] array = new double[size()];

		for (Observation<S, ?> observation : this) {
			array[observation.getObservedState().encode()] = encode(observation.getObservedValue());
		}

		return array;
	}

	private double encode(Object value) {
		return 0;
	}

}
