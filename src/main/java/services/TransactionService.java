package services;

import models.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by migho on 15.3.2017.
 */
public class TransactionService {

    public static Transaction getTransaction(int transactionId) {
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM transaction WHERE id=?", transactionId);
        return fetchTransaction(rs);
    }

    public static List<Transaction> getTransactions() {
        List<Transaction> list = new ArrayList<>();
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM transaction");
        try {
            while(rs.next()) {
                list.add(fetchTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<Transaction> saveTransactions(List<Transaction> list) {
        ArrayList<Transaction> returnList = new ArrayList<>();
        for(Transaction t : list) {
            SQLconnector.runSQLUpdate("INSERT INTO transaction (payer, archiving_code, date, " +
                    "reference_number, message, debtid, eventid, explanation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[] {t.getPayer(), t.getArchivingCode(), t.getDate(), t.getReferenceNumber(),
                    t.getMessage(), t.getDebtId(), t.getEventId(), t.getExplanation()});

            ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM transaction " +
                    "WHERE id = LAST_INSERT_ID()");
            try {
                if(rs.next()) {
                    returnList.add(fetchTransaction(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }

    private static Transaction fetchTransaction(ResultSet rs) {
        try {
            return new Transaction(rs.getInt("id"), rs.getString("payer"), rs.getString("archiving_code"),
                    rs.getDate("date").toString(), rs.getInt("reference_number"), rs.getString("message"),
                    rs.getInt("nbr"), rs.getInt("debtid"), rs.getInt("eventid"),
                    rs.getString("explanation"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
