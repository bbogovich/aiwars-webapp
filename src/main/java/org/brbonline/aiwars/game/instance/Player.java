package org.brbonline.aiwars.game.instance;

public class Player extends Actor{
	private static final long serialVersionUID = -1754924331289505945L;
	private int health=255;
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
}
