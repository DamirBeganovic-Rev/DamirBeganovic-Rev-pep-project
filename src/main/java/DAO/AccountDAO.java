package DAO;

import Model.Account;

public interface AccountDAO {
    
    // Select an Account from the database by its account_ID
    public Account selectAccountByAccountId(int account_id);

    // Select an Account from the database by its username
    public Account selectAccountByUsername(Account account);

    // Select an Account from the database by its username and password
    public Account selectAccountByUsernameAndPassword(Account account);

    // Insert a new Account into the database
    public Account insertAccount(Account account);

}