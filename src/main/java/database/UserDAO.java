package database;

import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static User getUser(int userId) {
        ResultSet rs = SQLSession.runSQLQuery("SELECT * FROM user WHERE id=?", userId);
        try {
            rs.next();
            return fetchUser(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User addUser(User u) {
        if(u.getId() == -1) {
            try {
                ResultSet rs = SQLSession.runSQLQuery("SELECT MIN(id) AS x FROM user");
                rs.next();
                u.setId(rs.getInt("x") - 1);
                SQLSession.runSQLUpdate("INSERT INTO user (id, username, name, mail) " +
                        "VALUES (?, ?, ?, ?)", new Object[]{u.getId(), u.getUsername(), u.getName(), u.getMail()});
                return u;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        SQLSession.runSQLUpdate("INSERT INTO user (username, name, mail) " +
                "VALUES (?, ?, ?)", new Object[]{u.getUsername(), u.getName(), u.getMail()});
        return u;
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
