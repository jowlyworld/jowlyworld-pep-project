package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public Message insertMessage(Message message) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) message.setMessage_id(rs.getInt(1));
        ps.close();
        return message;
    }

    public List<Message> getAllMessages() throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Message> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            ));
        }
        ps.close();
        return list;
    }

    public Message getMessageById(int id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Message msg = null;
        if (rs.next()) {
            msg = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
        }
        ps.close();
        return msg;
    }

    public Message deleteMessageById(int id) throws SQLException {
        Message msg = getMessageById(id);
        if (msg != null) {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        }
        return msg;
    }

    public Message updateMessageText(int id, String newText) throws SQLException {
        Message msg = getMessageById(id);
        if (msg != null) {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newText);
            ps.setInt(2, id);
            ps.executeUpdate();
            msg.setMessage_text(newText);
            ps.close();
        }
        return msg;
    }

    public List<Message> getMessagesByUserId(int userId) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        List<Message> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            ));
        }
        ps.close();
        return list;
    }
}
