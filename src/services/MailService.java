package services;

import models.Mail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by migho on 17.2.2017.
 */
public class MailService {

    public static List<Mail> getMails() {
        List<Mail> list = new ArrayList<>();
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM mail m JOIN debt d ON m.debtid = d.id " +
                "JOIN event e ON d.eventid = e.id JOIN user u ON d.userid = u.id");
        try {
            while(rs.next()) {
                list.add(fetchMail(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get mails with specified event number and status
     * @param eventNumber
     * @param status
     * @return
     */
    public static List<Mail> getMails(int eventNumber, String status) {
        List<Mail> list = new ArrayList<>();
        ResultSet rs;
        if(eventNumber == 0)
            rs = SQLconnector.runSQLQuery("SELECT * FROM mail m JOIN debt d ON m.debtid = d.id " +
                    "JOIN event e ON d.eventid = e.id JOIN user u ON d.userid = u.id WHERE m.status=?", status);
        else
            rs = SQLconnector.runSQLQuery("SELECT * FROM mail m JOIN debt d ON m.debtid = d.id " +
                    "JOIN event e ON d.eventid = e.id JOIN user u ON d.userid = u.id WHERE e.id=? AND m.status=?", new Object[] {eventNumber, status});
        try {
            while(rs.next()) {
                list.add(fetchMail(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int addMail(Mail m) {
        return SQLconnector.runSQLUpdate("INSERT INTO mail SET debtid=?, sender=?, subject=?," +
                        "message=?, status=?",
                new Object[] {m.getDebtId(), m.getSender(), m.getSubject(), m.getMessage(), m.getStatus()});
    }

    public static int updateMail(Mail m) {
        return SQLconnector.runSQLUpdate("UPDATE mail SET debtid=?, sender=?, subject=?, message=?, status=? WHERE id=?",
                m.getDebtId(), m.getSender(), m.getSubject(), m.getMessage(), m.getStatus(), m.getId());
    }

    private static Mail fetchMail(ResultSet rs) {
        try {
            return new Mail(
                    rs.getInt("id"),
                    rs.getInt("debtid"),
                    rs.getString("sender"),
                    rs.getString("mail"),
                    rs.getString("subject"),
                    rs.getString("message"),
                    rs.getString("status")
            );
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return new Mail(0, 0, "E", "E", "E", "E", "E");
    }

}
