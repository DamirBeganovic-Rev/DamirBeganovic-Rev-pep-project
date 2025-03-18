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
     * @param String message_id
     * @return Message a persisted Message from the databse
     */
    public Message getMessageByMessageId(String message_id){
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
     * @param String message_id
     * @return Message a persisted Message from the databse
     */
    public Message deleteMessageByMessageId(String message_id){
        if (messageDAO.selectMessageByMessageId(message_id) == null){
            return null;
        } else {
            Message deletedMessage = messageDAO.selectMessageByMessageId(message_id);
            return deletedMessage;
        }
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
