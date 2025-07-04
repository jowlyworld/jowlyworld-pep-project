package Service;

import DAO.AccountDAO;
import Model.Account;

import java.sql.SQLException;

public class AccountService {
    private AccountDAO dao;

    public AccountService(AccountDAO dao) {
        this.dao = dao;
    }

    public Account register(Account acc) throws SQLException {
        if (acc.getUsername() == null || acc.getUsername().isBlank() || acc.getPassword() == null || acc.getPassword().length() < 4)
            return null;
        if (dao.getAccountByUsername(acc.getUsername()) != null)
            return null;
        return dao.insertAccount(acc);
    }

    public Account login(Account acc) throws SQLException {
        return dao.getAccountByUsernameAndPassword(acc.getUsername(), acc.getPassword());
    }
}
