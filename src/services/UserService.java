package services;

import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public static User getUser(int userId) {
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM users WHERE id=?", userId);
        return fetchUser(rs);
    }

    private static User fetchUser(ResultSet rs) {
        try {
            return new User(rs.getInt("id"), rs.getString("username"), rs.getString("name"), rs.getString("mail"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
