package com.deckard.qlearning.space;

import java.util.ArrayList;

public class ObservationSpace<S extends IState> extends ArrayList<Observation<?, S>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7043303084059810213L;

	public double[] encode() {
		return new double[0];
	}

}
