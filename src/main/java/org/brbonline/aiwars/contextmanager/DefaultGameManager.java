package org.brbonline.aiwars.contextmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.brbonline.aiwars.exception.GameNameAlreadyInUseException;
import org.brbonline.aiwars.game.instance.DefaultGameInstance;
import org.brbonline.aiwars.game.instance.GameInstance;
import org.brbonline.aiwars.game.server.GameCommServer;
import org.brbonline.aiwars.game.server.GameSocketServer;
import org.brbonline.aiwars.game.server.GameWebSocketServer;
import org.brbonline.aiwars.game.user.UserSession;

/**
 * Manager for a game
 * Defines the socket servers, handles registration and unregistration of individual game instances and users
 */
public class DefaultGameManager implements GameManager {
	private static DefaultGameManager INSTANCE;
	
	private Logger logger = Logger.getLogger(DefaultGameManager.class);
	private Map<String,GameInstance> gameInstanceByNameMap = new HashMap<String,GameInstance>();
	private Map<Integer,GameInstance> gameInstanceByHashCode = new HashMap<Integer,GameInstance>();
	private Set<GameInstance> gameInstances = new HashSet<GameInstance>();
	private List<GameCommServer> commListeners = new ArrayList<GameCommServer>();
	private Map<String,UserSession> activeUsers = new HashMap<String,UserSession>();
	private int websocketPort=-1;
	private int socketPort=-1;
	private boolean initialized=false;
	private static Object shutdownMutex=new Object();
	
	/**
	 * Map of HTTP Session ID to UserSession
	 */
	private Map<String,UserSession> sessionMap = new HashMap<String,UserSession>();
	/*
	public DefaultGameManager(int websocketPort,int socketPort){
		this.websocketPort = websocketPort;
		this.socketPort = socketPort;
	}
	*/
	public static DefaultGameManager getInstance(){
		if(INSTANCE==null){
			INSTANCE = new DefaultGameManager();
		}
		return INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see org.brbonline.aiwars.contextmanager.GameManager#initialize()
	 * 
	 * Spring will call this twice.  Make sure it only executes once.
	 */
	public void initialize(){
		if(!initialized){
			if(websocketPort>0){
				GameWebSocketServer wsServer = new GameWebSocketServer(this,websocketPort);
				commListeners.add(wsServer);
				Thread wsServerThread = new Thread(wsServer);
				wsServerThread.setName("AIWars WebSocket Server on Port "+websocketPort);
				wsServerThread.start();
			}
			if(socketPort>0){
				GameSocketServer gsServer = new GameSocketServer(socketPort,this);
				commListeners.add(gsServer);
				Thread serverThread = new Thread(gsServer);
				serverThread.setName("AIWars Socket Server on Port "+socketPort);
				serverThread.start();
			}
			initialized=true;
		}
	}
	
	public GameInstance createGameInstance(String instanceName, UserSession creator) throws GameNameAlreadyInUseException{
		String creatorId = null;
		if(creator!=null){
			creatorId = creator.getHttpSessionId();
		}else{
			logger.warn("Unable to identify creator for new game instance!");
		}
		if(gameInstanceByNameMap.containsKey(instanceName)){
			throw new GameNameAlreadyInUseException();
		}
		logger.info("Creating a new GameInstance named \""+instanceName+"\"");
		GameInstance instance = new DefaultGameInstance(instanceName,creatorId);
		gameInstances.add(instance);
		gameInstanceByNameMap.put(instanceName, instance);
		gameInstanceByHashCode.put(instance.hashCode(),instance);
		Thread gameThread = new Thread(instance);
		gameThread.start();
		return instance;
	}
	
	public void shutdown(){
		logger.info("Shutting down game manager");
		synchronized(shutdownMutex){
			for (GameCommServer server:this.commListeners){
				try {
					server.shutdown();
					commListeners.remove(server);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			for (GameInstance instance:gameInstances){
				instance.teardown();
			}
		}
	}
	
	public Set<GameInstance> getGameInstances() {
		return gameInstances;
	}

	public void setGameInstances(Set<GameInstance> gameInstances) {
		this.gameInstances = gameInstances;
	}

	public List<GameCommServer> getCommListeners() {
		return commListeners;
	}

	public void setCommListeners(List<GameCommServer> commListeners) {
		this.commListeners = commListeners;
	}

	public Map<String, UserSession> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(Map<String, UserSession> activeUsers) {
		this.activeUsers = activeUsers;
	}

	public void addUserSession(UserSession userSession){
		String userName = userSession.getUserAccount().getUserLoginId();
		if(activeUsers.containsKey(userName)){
			logger.info("User "+userName+" has a pre-existing session.  Removing the old session.");
			UserSession oldSession = activeUsers.get(userName);
			sessionMap.remove(oldSession.getHttpSessionId());
			activeUsers.remove(userName);
		}
		String sessionId = userSession.getHttpSessionId();
		sessionMap.put(sessionId, userSession);
		activeUsers.put(userName, userSession);
		logger.info("A new UserSession has been added for user "+userName+" with sessionId "+sessionId);
	}
	
	public UserSession getUserSessionByHttpSessionId(String httpSessionId){
		return sessionMap.get(httpSessionId);
	}
	
	public GameInstance getGameInstanceByName(String gameName){
		return gameInstanceByNameMap.get(gameName);
	}

	public GameInstance getGameInstanceByHashCode(Integer gameSelection) {
		return gameInstanceByHashCode.get(gameSelection);
	}

	public int getWebsocketPort() {
		return websocketPort;
	}

	public void setWebsocketPort(int websocketPort) {
		this.websocketPort = websocketPort;
	}

	public int getSocketPort() {
		return socketPort;
	}

	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}
	
}
