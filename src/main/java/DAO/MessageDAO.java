package DAO;

import java.util.List;

import org.h2.command.query.Select;

import Model.Message;

public interface MessageDAO {

    // Select all messages from the database
    public List<Message> selectAllMessages();

    // Select a message from the database by its message_id
    public Message selectMessageByMessageId(int message_id);

    // Select all messages from the database by a specific user
    public List<Message> selectAllMessagesByUser(int account_id);

    // Delete a message from the database by its message_id
    public void deleteMessageByMessageId(int message_id);

    // Update a message in the database by its message_id
    public void updatedMessageByMessageId(int message_id, Message message);

    // Insert a new Message into the database
    public Message insertMessage(Message message);

}
