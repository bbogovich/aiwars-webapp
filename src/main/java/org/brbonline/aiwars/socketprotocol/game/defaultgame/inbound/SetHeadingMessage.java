package org.brbonline.aiwars.socketprotocol.game.defaultgame.inbound;

import org.brbonline.aiwars.socketprotocol.game.DefaultGameMessage;

public class SetHeadingMessage extends DefaultGameMessage {
	private static final long serialVersionUID = -7218522456784112616L;
	public final static int DIRECTION_CLOCKWISE=0;
	public final static int DIRECTION_COUNTER_CLOCKWISE=1;
	private double heading;
	private int direction;
	public static int getDIRECTION_CLOCKWISE() {
		return DIRECTION_CLOCKWISE;
	}
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
}
