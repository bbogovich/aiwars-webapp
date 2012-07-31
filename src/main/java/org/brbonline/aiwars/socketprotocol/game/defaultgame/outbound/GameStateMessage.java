package org.brbonline.aiwars.socketprotocol.game.defaultgame.outbound;

import java.util.Set;

import org.brbonline.aiwars.game.instance.Player;
import org.brbonline.aiwars.socketprotocol.game.DefaultGameMessage;

public class GameStateMessage extends DefaultGameMessage {
	private static final long serialVersionUID = -8421147484453770834L;
	private Set<Player> players;
	
	public Set<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	
}
