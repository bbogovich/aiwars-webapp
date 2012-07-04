package org.brbonline.aiwars.game.server;

import java.io.IOException;

/**
 * GameCommChannel defines a communication channel between the server and a remote player
 * 
 */
public interface GameCommServer {
	public void shutdown() throws IOException;
}
