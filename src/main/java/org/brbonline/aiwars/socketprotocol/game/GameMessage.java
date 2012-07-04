package org.brbonline.aiwars.socketprotocol.game;

public interface GameMessage {
	public long getTransactionId();
	public void setTransactionId(long transactionId);
	public String getMessageType();
}
