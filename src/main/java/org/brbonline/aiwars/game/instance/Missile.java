package org.brbonline.aiwars.game.instance;

public class Missile extends Actor{
	private static final long serialVersionUID = 2531017196820377763L;
	private int timeToLive;

	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
}
