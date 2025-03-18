package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

    /*
     * No args constructor for a messageService instantiates a plain messageDAO
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    /*
     * Constructor for a messageService when a messageDAO is provided
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
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


    /* // TODO : Test cases may be broken. Cases 2-4 seem to always pass 
     * //        No matter what
     * Creates a new Message to be added to the database
     * 
     * @param message an object representing a new Message.
     * @return the newly added Message if the add operation was successful, including the message_id. 
     * The returned Message is the persisted Message which is now stored in the database, and not
     * the parameter message which only exists in the application
     */
    public Message addMessage(Message message){
        
        if (message.getMessage_text().length() == 0){
            return null;
        } else if (message.getMessage_text().length() > 255){
            return null;
        } else if (message.getPosted_by() < 1){
            return null;
        }
        else {
            Message addedMessage = messageDAO.insertMessage(message);
            return addedMessage;
        }
    }
    
}
