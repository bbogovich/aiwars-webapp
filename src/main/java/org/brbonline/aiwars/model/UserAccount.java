package org.brbonline.aiwars.model;

import java.io.Serializable;
import java.sql.SQLException;

public class UserAccount implements Serializable
{
    private static final long serialVersionUID = -3977878760721099930L;

    private Long userKey;
    private String userLoginId;
    private String emailAddress;
    
    public UserAccount() throws SQLException{
    }
    
    public Long getUserKey( )
    {
        return userKey;
    }

    public void setUserKey( Long userKey )
    {
        this.userKey = userKey;
    }

    public String getUserLoginId( )
    {
        return userLoginId;
    }

    public void setUserLoginId( String userLoginId )
    {
        this.userLoginId = userLoginId;
    }

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}