package org.brbonline.aiwars.game.instance;

import java.io.Serializable;

public class Actor implements Serializable {
	private static final long serialVersionUID = -8406411921965374474L;

	private String userSessionId;
	
	private double positionX=0;
	private double positionY=0;
	private double heading=0;
	private double speed=0;
	
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
	public double getVelocityY() {
		double result=0;
		if(!(Double.compare(heading, 0)==0||Double.compare(heading,Math.PI)==0)){
			result = speed*Math.sin(heading);
		}
		return result;
	}
	public double getVelocityX() {
		double result=0;
		if(!(Double.compare(heading, Math.PI*0.5)==0||Double.compare(heading,Math.PI*1.5)==0)){
			result = speed*Math.cos(heading);
		}
		return result;
	}
}
