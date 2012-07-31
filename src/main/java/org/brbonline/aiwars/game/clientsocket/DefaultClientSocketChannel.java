package org.brbonline.aiwars.game.clientsocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.brbonline.aiwars.contextmanager.GameManager;
import org.brbonline.aiwars.game.user.UserSession;
import org.brbonline.aiwars.socketprotocol.game.GameMessage;
import org.brbonline.aiwars.socketprotocol.game.inbound.GameRegistrationMessage;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Base class with common functionality for ClientSocketChannel implementations.
 */
public abstract class DefaultClientSocketChannel implements ClientSocketChannel {
	protected GameManager gameManager;

	private ObjectMapper mapper = new ObjectMapper();
	private Logger logger = Logger.getLogger(DefaultClientSocketChannel.class);
	private UserSession userSession=null;
	
	private Object readMutex=new Object();
	private Object writeMutex=new Object();
	
	BlockingQueue<GameMessage> readQueue = new LinkedBlockingQueue<GameMessage>();
	BlockingQueue<GameMessage> writeQueue = new LinkedBlockingQueue<GameMessage>();
	
	/* (non-Javadoc)
	 * @see org.brbonline.aiwars.game.clientsocket.ClientSocketChannel#write(org.brbonline.aiwars.game.socketprotocol.game.GameMessage)
	 */
	public void write(GameMessage message) throws IOException{
		send(mapper.writeValueAsString(message));
	}
	
	/* (non-Javadoc)
	 * @see org.brbonline.aiwars.game.clientsocket.ClientSocketChannel#bufferedWrite(org.brbonline.aiwars.game.socketprotocol.game.GameMessage)
	 */
	public void bufferedWrite(GameMessage message){
		synchronized(writeMutex){
			writeQueue.add(message);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.brbonline.aiwars.game.clientsocket.ClientSocketChannel#flush()
	 */
	public void flush() throws IOException{
		synchronized(writeMutex){
			List<GameMessage> messages = new ArrayList<GameMessage>(writeQueue.size());
			writeQueue.drainTo(messages);
			this.send(mapper.writeValueAsString(messages));
		}
	}
	

	/* (non-Javadoc)
	 * @see org.brbonline.aiwars.game.clientsocket.ClientSocketChannel#readMessages()
	 */
	public List<GameMessage> readMessages() {
		List<GameMessage> messages;
		synchronized(readMutex){
			messages = new ArrayList<GameMessage>(readQueue.size());
			readQueue.drainTo(messages);
		}
		return messages;
	}

	/* (non-Javadoc)
	 * @see org.brbonline.aiwars.game.clientsocket.ClientSocketChannel#readNextMessage()
	 */
	public GameMessage readNextMessage() {
		GameMessage result;
		synchronized(readMutex){
			result=readQueue.poll();
		}
		return result;
	}
	
	/**
	 * @param message
	 * @return GameMessage of type matching the received message.  Unknown types return null and disconnect the socket
	 */
	private GameMessage parseReceivedMessage(String message){
		int nullSplit = message.indexOf("|");
		GameMessage request =null;
		String messageClassName = "org.brbonline.aiwars.socketprotocol."+message.substring(0,nullSplit);
		logger.info("Detected message type "+messageClassName);
		String messageBody = message.substring(nullSplit+1,message.length());
		try {
			request = (GameMessage)mapper.readValue(messageBody,Class.forName(messageClassName));
		} catch (ClassNotFoundException e) {
			logger.error("Error parsing message from client: "+e.getMessage(),e);
			disconnect();
		} catch (Exception e){
			logger.error("Error parsing message from client: "+e.getMessage(),e);
		}
		return request;
	}
	
	/**
	 * @param message Called when a new message is received on the underlying socket implementation.  Adds the message
	 * to the read buffer.
	 */
	protected void addReceivedMessage(String message) {
		logger.debug("Message Received: "+message);
		GameMessage receivedMessage = parseReceivedMessage(message);
		if(userSession==null){
			if(receivedMessage instanceof GameRegistrationMessage ){
				UserSession userSession = gameManager.getUserSessionByHttpSessionId(((GameRegistrationMessage) receivedMessage).getUserSessionId());
				if(userSession.getSocket()!=null){
					ClientSocketChannel oldSocket = userSession.getSocket();
					oldSocket.disconnect();
				}
				this.userSession = userSession;
				logger.debug("binding socket to user session");
				userSession.setSocket(this);
			}else{
				logger.warn("Message received prior to client registration.  Dropping message with text \n"+message);
			}
		}else{
			synchronized(readMutex){
				readQueue.add(receivedMessage);
			}
		}
	}
	
	public void setGameManager(GameManager manager){
		this.gameManager = manager;
	};
	
	/**
	 * Implementation-specific method to send a message to the client.
	 * 
	 * @param message
	 * @throws IOException
	 */
	protected abstract void send(String message) throws IOException;
}
