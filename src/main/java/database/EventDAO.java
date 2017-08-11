package database;


import java.sql.ResultSet;
import java.sql.SQLException;

public class EventDAO {

    public static String getEventDescription(int eventNumber) {
        return (String)getStringField(eventNumber, "description");
    }

    public static String getEventName(int eventNumber) {
        return (String)getStringField(eventNumber, "name");
    }

    public static int getAccountId(int eventNumber) {
        return getIntField(eventNumber, "accountid");
    }

    private static Integer getIntField(int eventNumber, String columnName) {
        ResultSet rs = SQLSession.runSQLQuery("SELECT * FROM event WHERE id=?;", new Object[] {eventNumber});
        if(rs != null) {
            try {
                if (rs.next()) {
                    return rs.getInt(columnName);
                } else {
                    System.out.println("No such event as " + eventNumber);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static String getStringField(int eventNumber, String columnName) {
        ResultSet rs = SQLSession.runSQLQuery("SELECT * FROM event WHERE id=?;", new Object[] {eventNumber});
        if(rs != null) {
            try {
                if (rs.next()) {
                    return rs.getString(columnName);
                } else {
                    System.out.println("No such event as " + eventNumber);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
