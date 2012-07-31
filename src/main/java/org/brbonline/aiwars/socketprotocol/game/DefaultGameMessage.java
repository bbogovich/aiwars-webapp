package org.brbonline.aiwars.socketprotocol.game;

import java.io.Serializable;

public abstract class DefaultGameMessage implements GameMessage,Serializable {

	private static final long serialVersionUID = -8822516527458982338L;
	private long transactionId;
	
	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getMessageType() {
		return this.getClass().getName().replace("org.brbonline.aiwars.socketprotocol.", "");
	}
}
