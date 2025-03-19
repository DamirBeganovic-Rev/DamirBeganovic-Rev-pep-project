package DAO;

import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;

public class AccountDAO {

    /*
     * Select an Account from the database by its account_ID
     * 
     * @param int account_id
     * @return Account A persisted account from  the database
     */
    public Account selectAccountByAccountId(int account_id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Account selectedAccount = new Account();
                selectedAccount.setAccount_id(resultSet.getInt("account_id"));
                selectedAccount.setUsername(resultSet.getString("username"));
                selectedAccount.setPassword(resultSet.getString("password"));
                
                return selectedAccount;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Select an Account from the database by its username
     * 
     * @param Account account An object modelling an Account.
     * @return Account A persisted account from  the database
     */
    public Account selectAccountByUsername(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE name = ?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Account selectedAccount = new Account();
                selectedAccount.setAccount_id(resultSet.getInt("account_id"));
                selectedAccount.setUsername(resultSet.getString("username"));
                selectedAccount.setPassword(resultSet.getString("password"));
                
                return selectedAccount;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Select an Account from the database by its username and password
     * 
     * @param Account account An object modelling an Account.
     * @return Account A persisted account from  the database
     */
    public Account selectAccountByUsernameAndPassword(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Account selectedAccount = new Account();
                selectedAccount.setAccount_id(resultSet.getInt("account_id"));
                selectedAccount.setUsername(resultSet.getString("username"));
                selectedAccount.setPassword(resultSet.getString("password"));
                
                return selectedAccount;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /*
     * Insert a new Account into the database
     * 
     * @param Account account A transient account object that does not have an account_id yet
     * @return Account A persisted account from  the database
     */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if  (resultSet.next()){
                int generated_account_id = (int) resultSet.getLong("account_id");
                
                Account insertedAccount = new Account();
                insertedAccount.setAccount_id(generated_account_id);
                insertedAccount.setUsername(account.getUsername());
                insertedAccount.setPassword(account.getPassword());
                
                return insertedAccount;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }
}
