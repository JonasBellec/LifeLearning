package com.deckard.qlearning.space;

import java.util.Random;

public abstract class Space<T extends Enum<T> & IEncodable> {

	protected Class<T> clazz;
	protected Random random = new Random();

	protected Space(Class<T> clazz) {
		this.clazz = clazz;
	}

	public int size() {
		return clazz.getEnumConstants().length;
	}

	public T random() {
		return clazz.getEnumConstants()[random.nextInt(size())];
	}

	public T decode(int code) {
		for (T t : clazz.getEnumConstants()) {
			if (t.encode() == code) {
				return t;
			}
		}
		return null;
	}
}
