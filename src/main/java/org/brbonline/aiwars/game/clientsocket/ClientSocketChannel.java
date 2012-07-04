package org.brbonline.aiwars.game.clientsocket;

import java.io.IOException;
import java.util.List;

import org.brbonline.aiwars.contextmanager.GameManager;
import org.brbonline.aiwars.socketprotocol.game.GameMessage;

/**
 * Communication channel between a client and the game server
 * 
 * Implementations wrap a Socket or WebSocket
 */
public interface ClientSocketChannel {
	/**
	 * Serializes the message to a JSON-encoded object and sends it to the client
	 * 
	 * @param message Message to send
	 * @throws IOException 
	 */
	public void write(GameMessage message) throws IOException;
	
	/**
	 * Serializes the message to a JSON-encoded object and buffers it for write to the client
	 * 
	 * Use ClientSocketChannel.flush() to write the buffer to the client.
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void bufferedWrite(GameMessage message) throws IOException;
	
	/**
	 * Write all buffered messages to the client.
	 * 
	 * @throws IOException
	 */
	public void flush() throws IOException;
	
	/**
	 * Retrieves all pending messages from the client
	 * 
	 * @return
	 */
	public List<GameMessage> readMessages();

	/**
	 * Retrieves next pending message from the client
	 * 
	 * @return
	 */
	public GameMessage readNextMessage();
	
	/**
	 * Terminate the underlying socket connection
	 */
	public void disconnect();
	
	public void setGameManager(GameManager manager);
}
