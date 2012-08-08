package org.brbonline.aiwars.game.instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.brbonline.aiwars.contextmanager.GameManager;
import org.brbonline.aiwars.game.clientsocket.ClientSocketChannel;
import org.brbonline.aiwars.game.user.UserSession;
import org.brbonline.aiwars.model.PlayerListItem;
import org.brbonline.aiwars.model.UserAccount;
import org.brbonline.aiwars.socketprotocol.game.GameMessage;
import org.brbonline.aiwars.socketprotocol.game.defaultgame.inbound.*;
import org.brbonline.aiwars.socketprotocol.game.defaultgame.outbound.GameStateMessage;
import org.brbonline.aiwars.socketprotocol.game.defaultgame.outbound.PlayerList;
import org.brbonline.aiwars.socketprotocol.game.inbound.PauseGameMessage;
import org.brbonline.aiwars.socketprotocol.game.inbound.ResumeGameMessage;
import org.brbonline.aiwars.socketprotocol.game.inbound.StartGameMessage;
import org.brbonline.aiwars.socketprotocol.game.outbound.GameStartedMessage;
import org.brbonline.aiwars.socketprotocol.game.outbound.RegistrationSuccessMessage;

public class DefaultGameInstance extends GameInstance {
	private Logger logger = Logger.getLogger(DefaultGameInstance.class);
	/**
	 * Width of game arena
	 */
	private int UNIVERSE_WIDTH=500;
	/**
	 * Height of game arena
	 */
	private int UNIVERSE_HEIGHT=500;
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
	private int SYNC_UPDATE_INTERVAL=10;
	/**
	 * Time to sleep between game frames
	 */
	private int FRAME_INTERVAL=20;
	
	private int frame=0;
	
	public DefaultGameInstance(String instanceName, String creatorName) {
		super(instanceName, creatorName);
		logger.info("Instantiating game instance \""+instanceName+"\"");
		setGameType("default");
	}
	
	private boolean gameStarted=false;
	private boolean gamePaused=false;
	private boolean instanceFinished=false;
	
	/**
	 * Run loop for the "idle" period before a game starts and after it completes
	 * Retrieve all pending input from clients and check for start request.
	 * @throws IOException 
	 */
	protected void runIdle() throws IOException{
		//logger.debug("runIdle");
		GameManager gameManager = this.getGameManager();
		for (Player player:this.players){
			UserSession session = gameManager.getUserSessionByHttpSessionId(player.getUserSessionId());
			if(session!=null){
				ClientSocketChannel socket = session.getSocket();
				if(socket!=null){
					List<GameMessage> messages = socket.readMessages();
					for (GameMessage message:messages){
						logger.info("DefaultGameInstance: processing message");
						if(message instanceof StartGameMessage){
							logger.info("runIdle:   starting game");
							startGame();
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Run loop for the "paused" state
	 * Retrieve all pending input from clients and check for resume request.
	 * @throws IOException 
	 */
	protected void runPaused() throws IOException{
		for (Player player:this.players){
			UserSession session = this.getGameManager().getUserSessionByHttpSessionId(player.getUserSessionId());
			List<GameMessage> messages = session.getSocket().readMessages();
			for (GameMessage message:messages){
				if(message instanceof ResumeGameMessage){
					logger.info("runPaused: resuming game");
					gamePaused=false;
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
	 * @throws IOException 
	 */
	protected void runMain() throws IOException{
		//logger.debug("runMain");
		
		
		GameManager gameManager = this.getGameManager();
		for (Player player:this.players){
			UserSession session = gameManager.getUserSessionByHttpSessionId(player.getUserSessionId());
			if(session!=null){
				ClientSocketChannel socket = session.getSocket();
				if(socket!=null){
					List<GameMessage> messages = socket.readMessages();
					for (GameMessage message:messages){
						if(message instanceof StartGameMessage){
							logger.info("runMain:   starting game");
							gameStarted=true;
							gamePaused=false;
							syncAllClients();
						} else if (message instanceof PauseGameMessage){
							gamePaused=true;
						} else if (message instanceof ResumeGameMessage){
							gamePaused=false;
						} else if (message instanceof SetHeadingMessage){
							player.setHeading(((SetHeadingMessage)message).getHeading());
						}
					}
				}
			}
		}
		if(gameStarted&&!gamePaused){
			//update player position
			for (Player player:this.players){
				double newX = player.getPositionX()+player.getVelocityX();
				if(newX>UNIVERSE_WIDTH){
					newX=0;
				}else if (newX < 0){
					newX = UNIVERSE_WIDTH;
				}
				player.setPositionX(newX);
				double newY = player.getPositionY()+player.getVelocityY();
				if(newY>UNIVERSE_HEIGHT){
					newY=0;
				}else if (newY < 0){
					newY = UNIVERSE_HEIGHT;
				}
				player.setPositionY(newY);
			}
			//check for collision
		}
		frame=(frame+1)%SYNC_UPDATE_INTERVAL;
		if(frame==0){
			try {
				syncAllClients();
			} catch (IOException e) {
				logger.error("IOException in client sync: "+e.getMessage(),e);
			}
		}
	}
	
	public void run() {
		logger.info("DefaultGameInstance:  starting run loop");
		while(!instanceFinished){
			if(!gameStarted){
				try {
					runIdle();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(gamePaused){
				try {
					runPaused();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					runMain();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(FRAME_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("DefaultGameInstance:  exiting run loop");
	}

	@Override
	public void startGame() throws IOException {
		logger.info("startGame()");
		syncAllClients();
		this.sendToAll(new GameStartedMessage());
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
		stopGame();
		
	}

	/* (non-Javadoc)
	 * @see org.brbonline.aiwars.game.instance.GameInstance#onUserAdd(org.brbonline.aiwars.game.user.UserSession)
	 */
	@Override
	protected void onUserAdd(UserSession userSession) {
		String sessionId = userSession.getHttpSessionId();
		logger.info("User Session "+sessionId+" added to game");
		if(this.userSessionToPlayerMap.containsKey(sessionId)){
			//user is already in the game.  this really shouldn't happen.  rebind user's player object.
			Player oldPlayer = this.userSessionToPlayerMap.get(sessionId);
			if(oldPlayer!=null){
				oldPlayer.setUserSessionId(sessionId);
			}else{
				initPlayer(sessionId);
			}
		} else {
			initPlayer(sessionId);
			try {
				this.sendPlayerList();
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}
	}

	protected Player initPlayer(String sessionId){
		Player newPlayer = new Player();
		newPlayer.setPositionX(Math.round(Math.random()*UNIVERSE_WIDTH));
		newPlayer.setPositionY(Math.round(Math.random()*UNIVERSE_HEIGHT));
		//assign player a random direction with speed 1
		newPlayer.setHeading(Math.random()*(2*Math.PI));
		//newPlayer.setHeading(0);
		newPlayer.setSpeed(1.0);
		newPlayer.setUserSessionId(sessionId);
		this.players.add(newPlayer);
		this.userSessionToPlayerMap.put(sessionId, newPlayer);
		return newPlayer;
	}
	
	protected void sendPlayerList() throws IOException{
		logger.info("sendPlayerList()");
		PlayerList playerList = new PlayerList();
		List<PlayerListItem> list = new ArrayList<PlayerListItem>(players.size());
		for (Player player:players){
			UserSession session = getUserSession(player);
			PlayerListItem item = new PlayerListItem();
			UserAccount userAccount = session.getUserAccount();
			if(userAccount!=null){
				item.setPlayerName(userAccount.getUserLoginId());
			}
			item.setSessionId(player.getUserSessionId());
			item.setUserKey(session.getUserId());
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
	
	protected void syncAllClients() throws IOException{
		GameStateMessage message = new GameStateMessage();
		message.setPlayers(this.players);
		this.sendToAll(message);
	}
}
