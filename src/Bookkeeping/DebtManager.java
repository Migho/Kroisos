package Bookkeeping;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DebtManager {

    public static List<Debt> getDebts() {
        List<Debt> list = new ArrayList<>();
        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Debt;", new Object[] {});
        try {
            while(rs.next()) {
                list.add(fetchDebt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Debt> getDebts(String status) {
        List<Debt> list = new ArrayList<>();
        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Debt WHERE status=?", new Object[] {status});
        try {
            while(rs.next()) {
                list.add(fetchDebt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static List<Debt> getEventDebtsWithZeroSentMessages(int event) {
        List<Debt> list = new ArrayList<>();
        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Debt WHERE status=?, due_date IS NULL;", new Object[] {"PENDING"});
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
        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Debt WHERE reference_number=?", new Object[] {referenceNumber});
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
        int result = SQLManager.runSQLUpdate("INSERT INTO Debt (reference_number, event, name, mail, sum, status, info)" +
                        "VALUES ?, ?, ?, ?, ?, ?, ?",
                new Object[] {d.getReferenceNumber(), d.eventNumber, d.name, d.mail, d.sum, "NOT_SENT", d.externalInfo});
        if(result != 1) System.out.println("Update affected " + result + " rows.");
        return 0;
    }

    public static int CreateEventDebts(String[] eMails, String[] names, int eventNumber, double sum) {
        if(eMails.length != names.length) {
            System.out.println(eMails.length +"!="+ names.length);
            return -80;
        }
        ReferenceNumberGenerator rGen = new ReferenceNumberGenerator(eventNumber);
        for(int i=0; i<eMails.length && i<names.length; i++) {
            Debt debt = new Debt();
            debt.eventNumber = rGen.getEventNumber();
            debt.participantNumber = rGen.getParticipantNumber();
            debt.checkDigit = rGen.getCheckDigit();
            debt.sum = sum;
            debt.mail = eMails[i];
            debt.name = names[i];
            addDebt(debt);
            rGen.nextReferenceNumber();
        }
        return 0;
    }
    public static int writeDebts() {
        //pitäis poistaa tai uudelleennimetä
        SQLManager.close();
        return 0;
    }
    public List<Debt> getAllEventDebts(int eventNumber) {
        List<Debt> list = new ArrayList<>();
        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Debt WHERE event=?;", new Object[] {eventNumber});
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
        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Debt WHERE reference_number=?;", new Object[] {referenceNumber});
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
                    return SQLManager.runSQLUpdate("UPDATE Debt SET status=? WHERE reference_number=?;", new Object[] {"AMOUNT_ERR", referenceNumber});
                }
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SQLManager.runSQLUpdate("UPDATE Debt SET status=? WHERE reference_number=?;", new Object[] {"PAID", referenceNumber});
    }

    private static Debt fetchDebt(ResultSet rs) {
        Debt debt = new Debt();
        try {
            int referenceNumber = rs.getInt("reference_number");
            debt.setReferenceNumber(referenceNumber);
            debt.name = rs.getString("name");
            debt.mail = rs.getString("mail");
            debt.eventNumber = rs.getInt("event");
            debt.sum = rs.getDouble("sum");
            debt.info = rs.getString("info");
            debt.dueDate = rs.getDate("due_date");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return debt;
    }

}