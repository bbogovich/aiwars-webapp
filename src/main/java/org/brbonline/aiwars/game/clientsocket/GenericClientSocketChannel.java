package org.brbonline.aiwars.game.clientsocket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.brbonline.aiwars.contextmanager.GameManager;
import org.brbonline.aiwars.game.server.GameSocketServer;

public class GenericClientSocketChannel extends DefaultClientSocketChannel implements Runnable {
	private Logger logger = Logger.getLogger(GenericClientSocketChannel.class);
	private GameSocketServer server;
	private boolean connected=false;
	private Socket socket;
	byte[] byteBuffer=new byte[0];
	DataOutputStream out;
	
	public GenericClientSocketChannel(GameSocketServer server,Socket socket, GameManager gameManager){
		this.server = server;
		this.socket=socket;
		this.setGameManager(gameManager);
	};
	
	public void disconnect(){
		connected=false;
	}
	
	public void run() {
		try{
			InputStream stream = socket.getInputStream();
			connected=true;
			while(connected){
				int byteCount = stream.available();
				if(byteCount>0){
					logger.debug(byteCount+" bytes are available for reading");
					byte[] oldByteBuffer = byteBuffer;
					int currentBufferLength = oldByteBuffer==null?0:oldByteBuffer.length;
					byteBuffer = new byte[currentBufferLength+byteCount];
					byte[] newBytes = new byte[byteCount];
					stream.read(newBytes, 0, byteCount);
					logger.debug(new String(newBytes, Charset.forName("UTF-8")));
					logger.debug("old byte buffer length: "+currentBufferLength);
					for (int i=0;i<currentBufferLength;i++){
						byteBuffer[i]=oldByteBuffer[i];
					}
					int newBufferLength=newBytes.length;
					logger.debug(new String(byteBuffer, Charset.forName("UTF-8")));
					for (int i=0;i<newBufferLength;i++){
						byteBuffer[currentBufferLength+i]=newBytes[i];
						if(newBytes[i]=='\n'){
							//handle message
							String message = new String(byteBuffer, Charset.forName("UTF-8")).substring(0, currentBufferLength+i).trim();
							logger.debug("completed message received of length "+(currentBufferLength+i)+":\n"+message);
							byteBuffer = new byte[newBufferLength-i];
							currentBufferLength=0;
						}
					}
					logger.debug(new String(byteBuffer, Charset.forName("UTF-8")));
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(socket.isConnected()){
				logger.info("Closing socket connection");
				socket.close();
			}
		}catch(Exception e){
			
		}
	}
	public void shutdown() throws IOException{
		connected=false;
		socket.close();
	}

	public GameSocketServer getServer() {
		return server;
	}

	@Override
	protected void send(String message) throws IOException {
		if(socket.isConnected()){
			OutputStream out = socket.getOutputStream();
			out.write((message+"\n").getBytes());
			out.flush();
		}
	}

}
