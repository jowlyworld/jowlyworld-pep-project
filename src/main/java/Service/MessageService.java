package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

import java.sql.SQLException;
import java.util.List;

public class MessageService {
    private MessageDAO msgDao;
    private AccountDAO accDao;

    public MessageService(MessageDAO msgDao, AccountDAO accDao) {
        this.msgDao = msgDao;
        this.accDao = accDao;
    }

    public Message create(Message msg) throws SQLException {
        if (msg.getMessage_text() == null || msg.getMessage_text().isBlank() || msg.getMessage_text().length() > 255)
            return null;
        if (accDao.getAccountById(msg.getPosted_by()) == null)
            return null;
        return msgDao.insertMessage(msg);
    }

    public List<Message> getAll() throws SQLException {
        return msgDao.getAllMessages();
    }

    public Message getById(int id) throws SQLException {
        return msgDao.getMessageById(id);
    }

    public Message delete(int id) throws SQLException {
        return msgDao.deleteMessageById(id);
    }

    public Message update(int id, String newText) throws SQLException {
        if (newText == null || newText.isBlank() || newText.length() > 255)
            return null;
        return msgDao.updateMessageText(id, newText);
    }

    public List<Message> getByUser(int userId) throws SQLException {
        return msgDao.getMessagesByUserId(userId);
    }
}
