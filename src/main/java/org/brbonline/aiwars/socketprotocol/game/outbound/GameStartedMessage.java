package org.brbonline.aiwars.socketprotocol.game.outbound;

import org.brbonline.aiwars.socketprotocol.game.DefaultGameMessage;
import org.brbonline.aiwars.socketprotocol.game.GameMessage;

/**
 * Message sent from server to client indicating that a game session has started.
 */
public class GameStartedMessage extends DefaultGameMessage implements
		GameMessage {
	private static final long serialVersionUID = 8063819820441024411L;

}
