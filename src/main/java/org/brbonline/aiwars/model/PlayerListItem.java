package org.brbonline.aiwars.model;

import java.io.Serializable;

public class PlayerListItem implements Serializable
{
    private static final long serialVersionUID = -1862010960416945928L;
    private String playerName;
    private String sessionId;
    private Long userKey;
    public String getPlayerName( )
    {
        return playerName;
    }
    public void setPlayerName( String playerName )
    {
        this.playerName = playerName;
    }
    public String getSessionId( )
    {
        return sessionId;
    }
    public void setSessionId( String sessionId )
    {
        this.sessionId = sessionId;
    }
    public Long getUserKey( )
    {
        return userKey;
    }
    public void setUserKey( Long userKey )
    {
        this.userKey = userKey;
    }
    
}
