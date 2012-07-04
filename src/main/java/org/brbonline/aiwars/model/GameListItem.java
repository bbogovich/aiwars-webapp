package org.brbonline.aiwars.model;

/**
 * Represents a game in a game list sent to the client
 */
public class GameListItem {
	private int id;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
