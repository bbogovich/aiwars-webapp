package org.brbonline.aiwars.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.brbonline.aiwars.model.UserAccount;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class UserAccountDaoImpl extends JdbcDaoSupport implements
        UserAccountDao
{
    public UserAccount findByUsernameAndPassword( final String username,final String password )
    {
        String sql = "SELECT * FROM USERS WHERE USERNAME=? AND USER_PASSWORD=?";
        Object[] args = {username,password};
        List<UserAccount> userAccountList = this.getJdbcTemplate().query( sql,args,new ParameterizedRowMapper<UserAccount>(){
            public UserAccount mapRow(ResultSet rs, int row) throws SQLException{
                UserAccount userAccount = new UserAccount();
                populateUserAccountFromResultSet(userAccount, rs);
                return userAccount;
            }
        });
        UserAccount result=null;
        if(userAccountList.size()==1){
            result = userAccountList.get( 0 );
        }else if(userAccountList.size()>1){
            logger.error("duplicate user account detected!");
        }
        return result;
    }
    
    private void populateUserAccountFromResultSet(UserAccount userAccount, ResultSet rs) throws SQLException{
        //userAccount.setEmailAddress(rs.getString("email_address"));
        userAccount.setUserKey( rs.getLong("id") );
        userAccount.setUserLoginId( rs.getString("username") );
    }

	public UserAccount findByUserKey(final Long userKey) {
        String sql = "SELECT * FROM USERS WHERE ID=?";
        Object[] args = {userKey};
        List<UserAccount> userAccountList = this.getJdbcTemplate().query( sql,args,new ParameterizedRowMapper<UserAccount>(){
            public UserAccount mapRow(ResultSet rs, int row) throws SQLException{
                UserAccount userAccount = new UserAccount();
                populateUserAccountFromResultSet(userAccount, rs);
                return userAccount;
            }
        });
        UserAccount result=null;
        if(userAccountList.size()==1){
            result = userAccountList.get( 0 );
        }else if(userAccountList.size()>1){
            logger.error("duplicate user account detected!");
        }
        return result;
	}

	public UserAccount findByUsername(String username) {
        String sql = "SELECT * FROM USERS WHERE USERNAME=?";
        Object[] args = {username};
        List<UserAccount> userAccountList = this.getJdbcTemplate().query( sql,args,new ParameterizedRowMapper<UserAccount>(){
            public UserAccount mapRow(ResultSet rs, int row) throws SQLException{
                UserAccount userAccount = new UserAccount();
                populateUserAccountFromResultSet(userAccount, rs);
                return userAccount;
            }
        });
        UserAccount result=null;
        if(userAccountList.size()==1){
            result = userAccountList.get( 0 );
        }else if(userAccountList.size()>1){
            logger.error("duplicate user account detected!");
        }
        return result;
	}
    
}