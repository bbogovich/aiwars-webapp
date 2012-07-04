package org.brbonline.aiwars.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.brbonline.aiwars.contextmanager.GameManager;
import org.brbonline.aiwars.game.clientsocket.GenericClientSocketChannel;

public class GameSocketServer implements GameCommServer,Runnable {

	private int port;
	private int connectionIndex = 0;
	private Logger logger = Logger.getLogger(GameSocketServer.class);
	private Set<GenericClientSocketChannel> connections = new HashSet<GenericClientSocketChannel>();
	private boolean connected;
	private ServerSocket listener=null;
	private GameManager gameManager;
	
	public GameSocketServer(int port, GameManager gameManager){
		this.port = port;
		this.gameManager = gameManager;
	}

	public void run() {
		// TODO Auto-generated method stub
		try{
			logger.debug("Starting server on port "+port);
			listener = new ServerSocket(port);
			connected=true;
			while(connected){
				logger.debug("Waiting for new connection");
				Socket socket = listener.accept();
				GenericClientSocketChannel connection = new GenericClientSocketChannel(this,socket,gameManager);
				connections.add(connection);
				Thread t = new Thread(connection);
				t.setName("websocket_"+(connectionIndex++));
				t.start();
			}
		} catch (IOException ioe) {
			logger.debug("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
	}
	
	public int getPort(){
		return port;
	}
	
	public void shutdown() throws IOException{
		logger.info("Shutting down socket server on port "+this.port);
		if(listener!=null){
			listener.close();
			for (GenericClientSocketChannel connection:connections){
				if(connection!=null){
					connection.disconnect();
				}
			}
		}
		connected=false;
	}
}
