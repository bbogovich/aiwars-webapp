package org.brbonline.aiwars.dao;

import org.brbonline.aiwars.model.UserAccount;

/**
 * DAO for read/write of UserAccount objects
 */
public interface UserAccountDao
{
    /**
     * @param username
     * @return UserAccount for the specified user name
     */
    public UserAccount findByUsername(String username);
    /**
     * @param username
     * @param password
     * @return UserAccount for the specified user name and password
     */
    public UserAccount findByUsernameAndPassword(String username,String password);
    /**
     * @param userKey
     * @return UserAccount for the specified userKey
     */
    public UserAccount findByUserKey(Long userKey);
}
