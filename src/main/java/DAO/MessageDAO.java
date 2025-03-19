package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    /*
     * Select all messages from the database
     * 
     * @return List<Message> a list of all messages persisted in the database
     */
    public List<Message> selectAllMessages(){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            List<Message> allMessages = new ArrayList<>();
            while (resultSet.next()){
                Message message = new Message();
                message.setMessage_id(resultSet.getInt("message_id"));
                message.setPosted_by(resultSet.getInt("posted_by"));
                message.setMessage_text(resultSet.getString("message_text"));
                message.setTime_posted_epoch(resultSet.getLong("time_posted_epoch"));
                
                allMessages.add(message);
            }
            return allMessages;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Select a message by its message_id
     * 
     * @param int message_id
     * @return Message a persisted message from the database
     */
    public Message selectMessageByMessageId(int message_id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Message selectedMessage = new Message();
                selectedMessage.setMessage_id(resultSet.getInt("message_id"));
                selectedMessage.setPosted_by(resultSet.getInt("posted_by"));
                selectedMessage.setMessage_text(resultSet.getString("message_text"));
                selectedMessage.setTime_posted_epoch(resultSet.getLong("time_posted_epoch"));
                
                return selectedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Get all messages by a specific user
     * 
     * @param int account_id
     * @return List<Message> a list of persisted message from the database from the specified user
     */
    public List<Message> selectAllMessagesByUser(int account_id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Message> allMessages = new ArrayList<>();
            while (resultSet.next()){
                Message selectedMessage = new Message();
                selectedMessage.setMessage_id(resultSet.getInt("message_id"));
                selectedMessage.setPosted_by(resultSet.getInt("posted_by"));
                selectedMessage.setMessage_text(resultSet.getString("message_text"));
                selectedMessage.setTime_posted_epoch(resultSet.getLong("time_posted_epoch"));

                allMessages.add(selectedMessage);
            }
            return allMessages;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Delete a message by its message_id
     * 
     * @param int message_id
     */
    public void deleteMessageByMessageId(int message_id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows affected by deleteMessageByMessageId()");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Update a message by its message_id
     * 
     * @param Int message_id
     */
    public void updatedMessageByMessageId(int message_id, Message message){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message_id);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows affected by updateMessageByMessageId()");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Insert a new Message into the database
     * 
     * @param Message message A transient message object that does not have a message_id yet
     * @return Message A persisted message from the database
     */
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                int generated_message_id = resultSet.getInt("message_id");
                
                Message insertedMessage = new Message();
                insertedMessage.setMessage_id(generated_message_id);
                insertedMessage.setPosted_by(message.getPosted_by());
                insertedMessage.setMessage_text(message.getMessage_text());
                insertedMessage.setTime_posted_epoch(message.getTime_posted_epoch());
                
                return insertedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
