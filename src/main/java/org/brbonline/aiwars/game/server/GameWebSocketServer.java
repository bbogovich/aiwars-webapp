package org.brbonline.aiwars.game.server;

import java.io.IOException;

import org.brbonline.aiwars.contextmanager.GameManager;
import org.brbonline.aiwars.game.clientsocket.WebsocketClientSocketChannel;

import websocket.WebSocket;
import websocket.server.DefaultWebSocketServer;

public class GameWebSocketServer extends DefaultWebSocketServer implements GameCommServer{

	private GameManager manager;
	
	public GameWebSocketServer(GameManager gameManager, int port){
		super(port);
		this.setManager(gameManager);
	}
	
	/* (non-Javadoc)
	 * @see websocket.server.WebSocketServer#onMessage(websocket.WebSocket, java.lang.String)
	 * 
	 * Excecuted when a new message is received on the websocket
	 * 
	 */
	public void onMessage(WebSocket websocket, String message)
			throws IOException {
		//if registration message, bind to User Session
	}

	/* 
	 * Executed when a new WebSocket connection has been established.
	 */
	public void onClientConnect(WebSocket websocket) throws IOException {
		// Associate the websocket with the game manager
		new WebsocketClientSocketChannel(websocket,this.manager);
	}

	public GameManager getManager() {
		return manager;
	}

	public void setManager(GameManager manager) {
		this.manager = manager;
	}

	public void shutdown() throws IOException {
		logger.info("Shutting down websocket server on port "+this.getPort());
		super.shutdown();
	}
}
