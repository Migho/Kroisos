package services;

import tools.ReferenceNumberGenerator;
import models.Debt;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DebtService {

    public static List<Debt> getDebts() {
        List<Debt> list = new ArrayList<>();
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM debt d LEFT JOIN user u ON d.userid = u.id");
        try {
            while(rs.next()) {
                list.add(fetchDebt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Debt> getDebts(int eventNumber) {
        List<Debt> list = new ArrayList<>();
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM debt d LEFT JOIN user u ON d.userid = u.id " +
                "WHERE eventid=?", eventNumber);
        try {
            while(rs.next()) {
                list.add(fetchDebt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Debt getDebt(long referenceNumber) {
        Debt debt = new Debt();
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM debt WHERE reference_number=?", referenceNumber);
        try {
            if(rs.next()) {
                debt = fetchDebt(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return debt;
    }


    public static int addDebt(Debt d) {
        int result = SQLconnector.runSQLUpdate("INSERT INTO debt (reference_number, eventid, userid, sum, info) " +
                        "VALUES (?, ?, ?, ?, ?)",
                new Object[] {d.getReferenceNumber(), d.getEventId(), d.getUser().getId(), d.getSum(), d.getInfo()});
        if(result != 1) ; //do some shit
        return 0;
    }

    public static int updateDebt(Debt d) {
        int result = SQLconnector.runSQLUpdate("UPDATE debt SET reference_number=?, eventid=?, userid=?, sum=?, due_date=?, status=?, last_mail_sent=?, info=?, penalty=? WHERE id=?",
                d.getReferenceNumber(), d.getEventId(), d.getUser().getId(), d.getSum(), d.getDueDate(), d.getStatus(), d.getLastMailSent(), d.getInfo(), d.getPenalty(), d.getId());
        if(result != 1) ; //do some shit
        return 0;
    }

    public static int createEventDebts(ArrayList<User> users, int eventId, int sum) {
        ReferenceNumberGenerator rGen = new ReferenceNumberGenerator(eventId);
        for(User u : users) {
            Debt d = new Debt();
            d.setReferenceNumber(rGen.getReferenceNumber());
            d.setEventId(eventId);
            d.setSum(sum);
            //USERID VOI OLLA MÄÄRITTELEMÄTTÄ!! Korjaa sitten kun members ja kroisos yhdistyy.
            d.setUser(u);
            addDebt(d);
            rGen.nextReferenceNumber();
        }
        return 0;
    }

    public static int writeDebts() {
        //pitäis poistaa tai uudelleennimetä
        SQLconnector.close();
        return 0;
    }
    public List<Debt> getAllEventDebts(int eventNumber) {
        List<Debt> list = new ArrayList<>();
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM Debt WHERE event=?;", eventNumber);
        try {
            while(rs.next()) {
                list.add(fetchDebt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int markDebtPaid(long referenceNumber, double sum) {
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM Debt WHERE reference_number=?;", referenceNumber);
        try {
            if(rs.next()) {
                String status = rs.getString("status");
                if(!status.equals("PENDING") && !status.equals("LATE") && !status.equals("ACCRUAL")) {
                    System.out.println("Requested to update " + status + " to PAID");
                    System.out.println("Status is not updated.");
                    return -1;
                }
                double debtSum = rs.getDouble("sum");
                if(debtSum > sum+0.005 || debtSum < sum-0.005) {
                    System.out.println("Requested to update cell with wrong amount");
                    System.out.println("Status is updated to AMOUNT_ERR.");
                    return SQLconnector.runSQLUpdate("UPDATE Debt SET status=? WHERE reference_number=?;", "AMOUNT_ERR", referenceNumber);
                }
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SQLconnector.runSQLUpdate("UPDATE Debt SET status=? WHERE reference_number=?;", "PAID", referenceNumber);
    }

    private static Debt fetchDebt(ResultSet rs) {
        Debt d = new Debt();
        try {
            int referenceNumber = rs.getInt("reference_number");
            d.setId(rs.getInt("id"));
            d.setReferenceNumber(referenceNumber);
            d.setEventId(rs.getInt("eventid"));
            d.setSum(rs.getInt("sum"));
            d.setInfo(rs.getString("info"));
            d.setDueDate(rs.getDate("due_date"));
            d.setStatus(rs.getString("status"));
            //koska molemmissa tauluissa on saman niminen sarake (ID) niin pitää hakea sitä sarakkeen numeron perusteella.
            //TODO korjaa sarakeongelma
            d.setUser(new User(rs.getInt(12), rs.getString("username"), rs.getString("name"), rs.getString("mail")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }

}