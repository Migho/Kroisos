package Bookkeeping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

public class BookkeepFixer {

    /**
     * This method will check if any debt "PENDING" status needs to be updated to "LATE" status
     * @param modifyPermissions When true, will also modify the status
     * @return The number of debts with updatable or updated status
     */
    public static int updateDebtStatus(boolean modifyPermissions) {
        int transactionDelay = 5; //What amount is good? Is this enough?
        int result = 0;

        ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Debt WHERE status=?", new Object[] {"PENDING"});
        Date currentDate = new Date();
        List<Integer> l = new ArrayList();
        try {
            while(rs.next()) {
                Date dueDate = rs.getDate("due_date");
                if(dueDate != null && (currentDate.getTime()/86400000-dueDate.getTime()/86400000) > transactionDelay)
                    l.add(rs.getInt("reference_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        if(modifyPermissions) for(Integer referenceNumber : l) {
            if(modifyPermissions) SQLManager.runSQLUpdate("UPDATE Debt SET status=? WHERE reference_number=?", new Object[] {"LATE", referenceNumber});
        }
        return l.size();
    }

}
