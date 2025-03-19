package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    /*
     * No args constructor for a messageService instantiates a plain messageDAO
     */
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    /*
     * Constructor for a messageService when a messageDAO is provided
     */
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    /*
     * Retrieve all messages
     * 
     * @return List<Message> a list of all messages in the database
     */
    public List<Message> getAllMessages(){
        List<Message> allMessages = messageDAO.selectAllMessages();
        return allMessages;
    }

    /*
     * Retrieve a specific message by its message_id
     * 
     * @param int message_id
     * @return Message a persisted Message from the database
     */
    public Message getMessageByMessageId(int message_id){
        // If a message with the given message_id doesnt exist, return a null object
        if (messageDAO.selectMessageByMessageId(message_id) == null){
            return null;
        } else {
            Message retrievedMessage = messageDAO.selectMessageByMessageId(message_id);
            return retrievedMessage;
        }
    }

    /*
     * Retrieve all messages from a specified user
     * 
     * @param int account_id
     * @return List<Message> a list of persisted Messages from the database from the speficied user
     */
    public List<Message> getAllMessagesByAccountId(int account_id){
        List<Message> allMessages = messageDAO.selectAllMessagesByUser(account_id);
        return allMessages;
    }

    /*
     * Delete a specific message by its message_id
     * 
     * @param int message_id
     * @return Message a transient message that has been deleted from the database
     */
    public Message deleteMessageByMessageId(int message_id){
        // If a message with the given message_id doesnt exist, return a null object
        if (messageDAO.selectMessageByMessageId(message_id) == null){
            return null;
        } else {
            Message deletedMessage = messageDAO.selectMessageByMessageId(message_id);
            return deletedMessage;
        }
    }

    /*
     * Update a specific message by its message_id
     * 
     * @param int message_id
     * @param Message message
     * @return Message an updated and persisted Message from the database
     */
    public Message updateMessageByMessageId(int message_id, Message message){
        // If an message with the given message_id doesnt exist, if the message_text is empty or > 255 chars, return a null object
        if (messageDAO.selectMessageByMessageId(message_id) == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255){
            return null;
        }
        // Get the existing message with the message_id from the database
        Message updatedMessage = messageDAO.selectMessageByMessageId(message_id);
        // Update the message by setting its message_text to the new test
        updatedMessage.setMessage_text(message.getMessage_text());
        // Execute the update operation on the database
        messageDAO.updatedMessageByMessageId(message_id, updatedMessage);
        // Return the newly updated messages from the database
        return messageDAO.selectMessageByMessageId(message_id);
    }

    /* 
     * Creates a new Message to be added to the database
     * 
     * @param Message message an object representing a new Message.
     * @return Message the newly added Message if the add operation was successful, including the message_id. 
     * The returned Message is the persisted Message which is now stored in the database, and not
     * the parameter message which only exists in the application
     */
    public Message addMessage(Message message){
        // Check to see if the message's posted_by ID refers to a real, existing user
        if (accountDAO.selectAccountByAccountId(message.getPosted_by()) == null){
            return null;
        }
        // Check to see if the message_text is not blank and not greater than 255
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255){
            return null;
        }
        // All requirements are met, so the message is created
        else {
            Message addedMessage = messageDAO.insertMessage(message);
            return addedMessage;
        }
    }
        
}
