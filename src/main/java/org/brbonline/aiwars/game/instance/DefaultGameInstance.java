package org.brbonline.aiwars.game.instance;

import java.io.IOException;
import java.util.List;

import org.brbonline.aiwars.game.user.UserSession;
import org.brbonline.aiwars.socketprotocol.game.GameMessage;
import org.brbonline.aiwars.socketprotocol.game.inbound.StartGameMessage;
import org.brbonline.aiwars.socketprotocol.game.outbound.RegistrationSuccessMessage;

public class DefaultGameInstance extends GameInstance {
	
	/**
	 * Width of game arena
	 */
	private int UNIVERSE_WIDTH=500;
	/**
	 * Height of game arena
	 */
	private int UNIVERSE_HEIGHT=500;
	/**
	 * Frequency to send synchronization updates to the client with the full world state
	 */
	private int SYNC_UPDATE_INTERVAL=200;
	/**
	 * Time to sleep between game frames
	 */
	private int FRAME_INTERVAL=20;
	
	public DefaultGameInstance(String instanceName, String creatorName) {
		super(instanceName, creatorName);
		setGameType("default");
	}
	
	private boolean gameStarted=false;
	private boolean gamePaused=false;
	private boolean instanceFinished=false;
	
	/**
	 * Run loop for the "idle" period before a game starts and after it completes
	 * Retrieve all pending input from clients and check for start request.
	 */
	protected void runIdle(){
		for (Player player:this.players){
			UserSession session = player.getUserSession();
			List<GameMessage> messages = session.getSocket().readMessages();
			for (GameMessage message:messages){
				if(message instanceof StartGameMessage){
					gameStarted=true;
				}
			}
		}
	}
	
	
	/**
	 * Run loop for the "paused" state
	 * Retrieve all pending input from clients and check for resume request.
	 */
	protected void runPaused(){
		for (Player player:this.players){
			UserSession session = player.getUserSession();
			List<GameMessage> messages = session.getSocket().readMessages();
			for (GameMessage message:messages){
				if(message instanceof StartGameMessage){
					gameStarted=true;
				}
			}
		}
	}
	
	/**
	 * Run loop for the "paused" state
	 * Retrieve all pending input from clients and check for resume request.
	 */
	protected void runMain(){
		for (Player player:this.players){
			UserSession session = player.getUserSession();
			List<GameMessage> messages = session.getSocket().readMessages();
			for (GameMessage message:messages){
				if(message instanceof StartGameMessage){
					gameStarted=true;
				}
			}
		}
	}
	
	public void run() {
		while(!instanceFinished){
			if(!gameStarted){
				runIdle();
			} else if(gamePaused){
				runPaused();
			} else {
				runMain();
			}
			try {
				Thread.sleep(FRAME_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void teardown() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.brbonline.aiwars.game.instance.GameInstance#onUserAdd(org.brbonline.aiwars.game.user.UserSession)
	 */
	@Override
	protected void onUserAdd(UserSession userSession) {
		String sessionId = userSession.getHttpSessionId();
		if(this.userSessionToPlayerMap.containsKey(sessionId)){
			//user is already in the game.  this really shouldn't happen.  rebind user's player object.
			Player oldPlayer = this.userSessionToPlayerMap.get(sessionId);
			if(oldPlayer!=null){
				oldPlayer.setUserSession(userSession);
			}else{
				Player newPlayer = new Player();
				newPlayer.setUserSession(userSession);
				this.players.add(newPlayer);
				this.userSessionToPlayerMap.put(sessionId, newPlayer);
			}
		} else {
			Player newPlayer = new Player();
			newPlayer.setUserSession(userSession);
			this.players.add(newPlayer);
			this.userSessionToPlayerMap.put(sessionId, newPlayer);
		}
	}

	@Override
	public void onUserSocketConnected(UserSession userSession) {
		try {
			userSession.getSocket().write(new RegistrationSuccessMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
