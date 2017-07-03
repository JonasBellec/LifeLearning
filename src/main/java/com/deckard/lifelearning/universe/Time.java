package com.deckard.lifelearning.universe;

public class Time {
	private Integer day;
	private Integer hour;

	public Time() {
		super();
		this.day = 0;
		this.hour = 0;
	}

	public Time(Integer day, Integer hour) {
		super();
		this.day = day;
		this.hour = hour;
	}

	public Time(Time time) {
		super();
		this.day = time.day;
		this.hour = time.hour;
	}

	public void tick() {
		hour++;
		if (hour >= 24) {
			day++;
			hour = 0;
		}
	}

	/**
	 * @return the day
	 */
	public Integer getDay() {
		return day;
	}

	/**
	 * @return the hour
	 */
	public Integer getHour() {
		return hour;
	}
}
