package org.brbonline.aiwars.game.instance;

public class Player {
	private String userSessionId;
	
	private double positionX=0;
	private double positionY=0;
	private double heading=0;
	private double speed=0;
	private int health=255;
	
	public String getUserSessionId() {
		return userSessionId;
	}
	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
	public double getPositionX() {
		return positionX;
	}
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}
	public double getPositionY() {
		return positionY;
	}
	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getVelocityX() {
		double result=0;
		if(!(Double.compare(heading, Math.PI*0.5)==0||Double.compare(heading,Math.PI*1.5)==0)){
			double sinAngle = Math.sin(heading);
			return /*((heading>Math.PI*0.5&&heading<Math.PI*1.5)?-1:1)**/Math.sqrt(speed*speed-sinAngle*sinAngle);
		}
		return result;
	}
	public double getVelocityY() {
		double result=0;
		if(!(Double.compare(heading, 0)==0||Double.compare(heading,Math.PI)==0)){
			double cosAngle = Math.cos(heading);
			return /*((heading>Math.PI&&heading<Math.PI*2)?-1:1)**/Math.sqrt(speed*speed-cosAngle*cosAngle);
		}
		return result;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
}
