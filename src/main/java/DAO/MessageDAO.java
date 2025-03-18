package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    /*
     * Insert a new Message into the database
     * 
     * @param message A transient message object that does not have a message_id yet
     * @return Message A persisted message from  the database
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
                int generated_message_id = (int) resultSet.getLong("message_id");
                int posted_by_id = (int) resultSet.getInt("posted_by");
                long time_posted = (long) resultSet.getLong("time_posted_epoch");
                Message insertedMessage = new Message();
                insertedMessage.setMessage_id(generated_message_id);
                insertedMessage.setPosted_by(posted_by_id);
                insertedMessage.setMessage_text(resultSet.getString("message_text"));
                insertedMessage.setTime_posted_epoch(time_posted);
                return insertedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
