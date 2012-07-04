package org.brbonline.aiwars.game.clientsocket;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.brbonline.aiwars.contextmanager.GameManager;

import websocket.WebSocket;
import websocket.WebSocketMessageListener;

public class WebsocketClientSocketChannel extends DefaultClientSocketChannel implements WebSocketMessageListener{
	private WebSocket websocket=null;
	
	private Logger logger = Logger.getLogger(WebsocketClientSocketChannel.class);
	
	public WebsocketClientSocketChannel(WebSocket websocket, GameManager manager){
		websocket.addMessageListener(this);
		this.websocket = websocket;
		this.setGameManager(manager);
	}

	public void onWebSocketMessage(String message) {
		logger.info(message);
		this.addReceivedMessage(message);
	}

	@Override
	protected void send(String message) throws IOException {
		websocket.send(message);
	}

	public void disconnect() {
		try {
			websocket.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
}
