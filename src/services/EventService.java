package services;


import java.sql.ResultSet;
import java.sql.SQLException;

public class EventService {

    public static String getEventDescription(int eventNumber) {
        return getStringField(eventNumber, "description");
    }

    public static String getEventName(int eventNumber) {
        return getStringField(eventNumber, "name");
    }

    //delete this shit
    public static String getType(int eventNumber) {
        ResultSet rs = SQLconnector.runSQLQuery("SELECT Account.name FROM event INNER JOIN Account WHERE event_number=? AND Event.account = Account.id;", new Object[] {eventNumber});
        if(rs != null) {
            try {
                if (rs.next()) {
                    return rs.getString("Account.name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "Undefined";
    }

    private static String getStringField(int eventNumber, String columnName) {
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM event WHERE id=?;", new Object[] {eventNumber});
        if(rs != null) {
            try {
                if (rs.next()) {
                    return rs.getString(columnName);
                } else {
                    System.out.println("No such event");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
