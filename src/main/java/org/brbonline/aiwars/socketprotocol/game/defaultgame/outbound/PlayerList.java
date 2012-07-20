package org.brbonline.aiwars.socketprotocol.game.defaultgame.outbound;

import java.util.List;

import org.brbonline.aiwars.model.PlayerListItem;
import org.brbonline.aiwars.socketprotocol.game.DefaultGameMessage;
import org.brbonline.aiwars.socketprotocol.game.GameMessage;

public class PlayerList extends DefaultGameMessage implements GameMessage {
	private static final long serialVersionUID = 8960049524539353068L;
	private List<PlayerListItem> players;
	public List<PlayerListItem> getPlayers() {
		return players;
	}
	public void setPlayers(List<PlayerListItem> players) {
		this.players = players;
	}
}
