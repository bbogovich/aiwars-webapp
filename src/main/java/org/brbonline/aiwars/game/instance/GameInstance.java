package org.brbonline.aiwars.game.instance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.brbonline.aiwars.game.user.UserSession;

public abstract class GameInstance implements Runnable {
	
	private String instanceName;
	private String creatorName;
	private String gameType;
	protected Set<Player> players;
	protected Map<String,Player> userSessionToPlayerMap;
	
	public GameInstance(String instanceName, String creatorName){
		this.instanceName = instanceName;
		this.creatorName = creatorName;
		this.players = new HashSet<Player>();
		this.userSessionToPlayerMap = new HashMap<String,Player>();
	}
	
	private Set<UserSession> gameUserSessions = new HashSet<UserSession>();
	
	public void addUserToGame(UserSession userSession){
		gameUserSessions.add(userSession);
		userSession.setActiveGame(this);
		onUserAdd(userSession);
	}
	
	public abstract void onUserSocketConnected(UserSession userSession);
	
	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Set<UserSession> getGameUserSessions() {
		return gameUserSessions;
	}

	public void setGameUserSessions(Set<UserSession> gameUserSessions) {
		this.gameUserSessions = gameUserSessions;
	}
	
	public abstract void startGame();
	public abstract void stopGame();
	public abstract void teardown();
	
	protected abstract void onUserAdd(UserSession userSession);

	public String getGameType() {
		return gameType;
	}

	protected void setGameType(String gameType) {
		this.gameType = gameType;
	}
}
