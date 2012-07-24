package org.brbonline.aiwars.game.instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.brbonline.aiwars.game.user.UserSession;
import org.brbonline.aiwars.model.PlayerListItem;
import org.brbonline.aiwars.socketprotocol.game.GameMessage;
import org.brbonline.aiwars.socketprotocol.game.defaultgame.outbound.PlayerList;
import org.brbonline.aiwars.socketprotocol.game.inbound.PauseGameMessage;
import org.brbonline.aiwars.socketprotocol.game.inbound.StartGameMessage;
import org.brbonline.aiwars.socketprotocol.game.outbound.RegistrationSuccessMessage;

public class DefaultGameInstance extends GameInstance {
	private Logger logger = Logger.getLogger(DefaultGameInstance.class);
	/**
	 * Width of game arena
	 */
	private int UNIVERSE_WIDTH=1000;
	/**
	 * Height of game arena
	 */
	private int UNIVERSE_HEIGHT=1000;
	/**
	 * Starting positions for players
	 * */
	private int[][] startPositions={
			{100,100},
			{900,900}
	};
	/**
	 * Frequency to send synchronization updates to the client with the full world state
	 */
	private int SYNC_UPDATE_INTERVAL=200;
	/**
	 * Time to sleep between game frames
	 */
	private int FRAME_INTERVAL=20;
	
	private long lastSyncTime=0L;
	
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
	 * Primary run loop for the game.
	 * For each player, process all inputs.
	 * Move all players.
	 * If the update interval has been reached, send the world state.
	 * 
	 * Retrieve all pending input from clients and check for resume request.
	 */
	protected void runMain(){
		for (Player player:this.players){
			UserSession session = player.getUserSession();
			List<GameMessage> messages = session.getSocket().readMessages();
			for (GameMessage message:messages){
				if(message instanceof StartGameMessage){
					gameStarted=true;
				} else if (message instanceof PauseGameMessage){
					gamePaused=true;
				}
			}
		}
		if(gameStarted&&!gamePaused){
			//update player position
			//check for collision
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
		logger.info("startGame()");
		gameStarted=true;
	}

	@Override
	public void stopGame() {
		// TODO Auto-generated method stub
		gameStarted=false;
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
				newPlayer.setPositionX(Math.round(Math.random()*UNIVERSE_WIDTH));
				newPlayer.setPositionY(Math.round(Math.random()*UNIVERSE_WIDTH));
				newPlayer.setUserSession(userSession);
				this.players.add(newPlayer);
				this.userSessionToPlayerMap.put(sessionId, newPlayer);
			}
		} else {
			Player newPlayer = new Player();
			newPlayer.setUserSession(userSession);
			this.players.add(newPlayer);
			this.userSessionToPlayerMap.put(sessionId, newPlayer);
			try {
				this.sendPlayerList();
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}
	}

	protected void sendPlayerList() throws IOException{
		PlayerList playerList = new PlayerList();
		List<PlayerListItem> list = new ArrayList<PlayerListItem>(players.size());
		for (Player player:players){
			PlayerListItem item = new PlayerListItem();
			item.setPlayerName(player.getUserSession().getUserAccount().getUserLoginId());
			item.setSessionId(player.getUserSessionId());
			item.setUserKey(player.getUserSession().getUserId());
			list.add(item);
		}
		this.sendToAll(playerList);
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
