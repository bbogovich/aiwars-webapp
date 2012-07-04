package org.brbonline.aiwars.contextmanager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.brbonline.aiwars.exception.GameNameAlreadyInUseException;
import org.brbonline.aiwars.game.instance.DefaultGameInstance;
import org.brbonline.aiwars.game.instance.GameInstance;
import org.brbonline.aiwars.game.server.GameCommServer;
import org.brbonline.aiwars.game.server.GameSocketServer;
import org.brbonline.aiwars.game.server.GameWebSocketServer;
import org.brbonline.aiwars.game.user.UserSession;

public interface GameManager {
	public void initialize();
	public GameInstance createGameInstance(String instanceName, UserSession creator) throws GameNameAlreadyInUseException;	
	public void shutdown();
	public Set<GameInstance> getGameInstances();
	public void setGameInstances(Set<GameInstance> gameInstances);

	public List<GameCommServer> getCommListeners();

	public void setCommListeners(List<GameCommServer> commListeners);

	public Map<String, UserSession> getActiveUsers();

	public void setActiveUsers(Map<String, UserSession> activeUsers);

	public void addUserSession(UserSession userSession);
	public UserSession getUserSessionByHttpSessionId(String httpSessionId);
	
	public GameInstance getGameInstanceByName(String gameName);

	public GameInstance getGameInstanceByHashCode(Integer gameSelection);

	public int getWebsocketPort();

	public void setWebsocketPort(int websocketPort);

	public int getSocketPort();

	public void setSocketPort(int socketPort);
}
