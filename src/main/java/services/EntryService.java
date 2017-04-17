package services;

import models.Entry;
import models.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by migho on 20.3.2017.
 */
public class EntryService {

    public static Entry getTransaction(int transactionId) {
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM entry WHERE id=?", transactionId);
        return fetchTransaction(rs);
    }

    public static List<Entry> getTransactions() {
        List<Entry> list = new ArrayList<>();
        ResultSet rs = SQLconnector.runSQLQuery("SELECT * FROM entry");
        try {
            while(rs.next()) {
                list.add(fetchTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveEntries(ArrayList<Transaction> list, int targetAccountId) {
        String statement = "INSERT INTO entry (account_id, sum, transaction_id, debit) VALUES (?, ?, ?, ?)";
        for(Transaction t : list) {
            int accountId = EventService.getAccountId(t.getEventId());
            if (t.getSum() > 0) {
                SQLconnector.runSQLUpdate(statement, new Object[]{targetAccountId, t.getSum(), t.getId(), true});
                if (accountId != 0) {
                    SQLconnector.runSQLUpdate(statement, new Object[]{accountId, t.getSum(), t.getId(), false});
                }
            } else {
                SQLconnector.runSQLUpdate(statement, new Object[]{targetAccountId, t.getSum(), t.getId(), false});
                if (accountId != 0) {
                    SQLconnector.runSQLUpdate(statement, new Object[]{accountId, t.getSum(), t.getId(), true});
                }
            }
        }
    }

    private static Entry fetchTransaction(ResultSet rs) {
        try {
            return new Entry(rs.getInt("id"), rs.getInt("account_id"), rs.getInt("sum"),
                    rs.getInt("transaction_id"), rs.getBoolean("debit"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
