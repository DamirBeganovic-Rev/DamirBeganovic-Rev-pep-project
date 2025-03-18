package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    /*
     * No args constructor for an accountService instantiates a plain accountDAO
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /*
     * Constructor for an accountService when an accountDAO is provided
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /*
     * Creates a new Account to be added to the database
     * 
     * @param Account account an object representing a new Account.
     * @return Account the newly added Account if the add operation was successful, including the account_id. 
     * The returned Account is the persisted Account which is now stored in the database, and not
     * the parameter account which only exists in the application
     */
    public Account addAcount(Account account){
        // Check to see if an account with this username already exists
        if (accountDAO.selectAccountByUsername(account) != null){
            return null;
        } 
        // Check to see if the username is not blank and the password is at least 4 characters long
        if (account.getUsername().length() == 0 || account.getPassword().length() < 4){
            return null;
        }
        // All requirements are met, so the account is created
        else {
            Account addedAccount = accountDAO.insertAccount(account);
            return addedAccount;
        }
    }

    /*
     * Logs a user into their account
     * 
     * @param Account account an object representing a user's Account.
     * @return Account the logged in Account if the login operation was successful (an an account with
     * the supplied username and password exists in the database), including the account_id. 
     * The returned Account is the persisted Account which is stored in the database, and not
     * the parameter account which only exists in the application
     */
    public Account login(Account account){
        if (accountDAO.selectAccountByUsernameAndPassword(account) != null){
            Account loggedinAccount = accountDAO.selectAccountByUsernameAndPassword(account);
            return loggedinAccount;
        } else {
            return null;
        }
    }

}
