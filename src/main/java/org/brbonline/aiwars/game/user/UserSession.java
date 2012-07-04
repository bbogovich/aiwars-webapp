package org.brbonline.aiwars.game.user;

import java.io.Serializable;
import java.util.Date;

import org.brbonline.aiwars.game.clientsocket.ClientSocketChannel;
import org.brbonline.aiwars.game.instance.GameInstance;
import org.brbonline.aiwars.model.UserAccount;

/**
 * Defines a user session in the context of the application.  Not to be confused with HTTPSession.
 */
public class UserSession implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1482951114888796276L;
	private UserAccount userAccount;
	private Date lastActivity;
	private String httpSessionId;
	private ClientSocketChannel socket;
	private GameInstance activeGame;
	
	
	public Long getUserId() {
		return userAccount.getUserKey();
	}
	public Date getLastActivity() {
		return lastActivity;
	}
	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}
	public String getHttpSessionId() {
		return httpSessionId;
	}
	public void setHttpSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}
	public UserAccount getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	public ClientSocketChannel getSocket() {
		return socket;
	}
	public void setSocket(ClientSocketChannel socket) {
		this.socket = socket;
		if(activeGame!=null){
			activeGame.onUserSocketConnected(this);
		}
	}
	public void setActiveGame(GameInstance gameInstance) {
		activeGame = gameInstance;
	}
	public GameInstance getActiveGame(){
		return activeGame;
	}
	
}
