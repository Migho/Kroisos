package Bookkeeping;

import Tools.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by migho on 2.11.2016.
 */
public class EventManager {

    public static String getEventDescription(int eventNumber) {
        return getStringField(eventNumber, "description");
    }

    public static String getEventName(int eventNumber) {
        return getStringField(eventNumber, "name");
    }

    public static String getType(int eventNumber) {
        ResultSet rs = SQLManager.runSQLQuery("SELECT Account.name FROM Event INNER JOIN Account WHERE event_number=? AND Event.account = Account.id;", new Object[] {eventNumber});
        try {
            if (rs.next()) {
                return rs.getString("Account.name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Undefined";
    }

    private static String getStringField(int eventNumber, String columnName) {
        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Event WHERE event_number=?;", new Object[] {eventNumber});
        try {
            if(rs.next()) {
                return rs.getString(columnName);
            } else {
                System.out.println("No such event");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

}
