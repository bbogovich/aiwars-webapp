package org.brbonline.aiwars.socketprotocol.game.inbound;

import org.brbonline.aiwars.socketprotocol.game.DefaultGameMessage;

/**
 * Message sent from client when a new ClientSocketChannel is opened to bind the communication channel
 * to a user session.
 */
public class GameRegistrationMessage extends DefaultGameMessage {
	
	private static final long serialVersionUID = 3291558113673113590L;
	private String userSessionId;
	private String userType;
	
	public GameRegistrationMessage() {
		// TODO Auto-generated constructor stub
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}

	public void setUserType(String userType){
		this.userType = userType;
	}
	
	public String getUserType(){
		return userType;
	}
}
