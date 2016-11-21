package Mail;

import Bookkeeping.Debt;
import Bookkeeping.EventManager;
import Bookkeeping.SQLManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by migho on 28.9.2016.
 */
public class MessageCreator {

    TextFileReader tReader;
    SQLManager sqlManager;
    int expDays = 14;
    SessionStarter sStarter;
    EventManager eManager = new EventManager();
    
    
    public MessageCreator(String file) throws java.io.FileNotFoundException {
        sqlManager = new SQLManager();
        tReader = new TextFileReader();
        if(tReader.readFile(file) != 0) {
            System.out.println("Couldnt read or find the file: " + file);
            throw new java.io.FileNotFoundException();
        }
    }

    /**
     * Set new debt expiration time in days.
     * @param expDays Days the user has time to pay
     */
    public void setExpDays(int expDays) {
        if(expDays<0) return;
        this.expDays = expDays;
    }

    /**
     * Will create and send new payment instructions (In other words, debts which has no due date).
     * @param list List of Debts
     * @return will return all unsuccessfully sent debts. Null if invalid file was specified in constructor.
     */
    public List<Debt> sendPaymentInstructions(List<Debt> list) {
        return sendSetOfMails(list, true, false);
    }

    /**
     * Will create and send new payment reminders (In other words, debts which has status LATE).
     * @param list List of Debts
     * @return will return all unsuccessfully sent debts. Null if invalid file was specified in constructor.
     */
    public List<Debt> sendPaymentReminder(List<Debt> list) {
        return sendSetOfMails(list, false, true);
    }

    //Method for sending list of mails. Uses "formAndSendMail" -method for each debt.
    private List<Debt> sendSetOfMails(List<Debt> list, boolean onlyNullDueDate, boolean onlyLateStatus) {
        if(tReader == null) return null;
        List<Debt> unsuccessfulMails = new ArrayList<>();
        sStarter = new SessionStarter(1);             //set 0 to disable preview

        for(Debt d : list) {
            ResultSet rs = SQLManager.runSQLQuery("SELECT * FROM Debt WHERE reference_number="+d.getReferenceNumber()+";");
            try {
                if(rs.next()) {
                    Date date = rs.getDate("due_date");
                    String status = rs.getString("status");
                    if(date == null && onlyNullDueDate) {
                        int result = formAndSendMail(d);
                        if(result < 0) unsuccessfulMails.add(d);
                    } else if(date != null && status.equals("LATE") && onlyLateStatus) {
                        d.dueDate = date;
                        int result = formAndSendMail(d);
                        if(result < 0) unsuccessfulMails.add(d);
                    }
                    /*It shouldn't be necessary to worry about null date + late status at the same
                    time, since debt without due date means no payment instructions are send.*/
                }
            } catch (SQLException e) {
                System.out.println("No debt found with this reference number");
                e.printStackTrace();
            }
        }
        return unsuccessfulMails;
    }

    //This method will take care of forming and sending one message, created from the debt.
    private int formAndSendMail(Debt d) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> changeList = new HashMap<>();
        changeList.put("§REFERENCENUMBER§", Integer.toString(d.getReferenceNumber()));
        changeList.put("§NAME§", d.name);
        changeList.put("§SUM§", String.format("%.2f", d.sum));
        changeList.put("§EVENTNAME§", eManager.getEventName(d.eventNumber));
        changeList.put("§EVENTDESC§", eManager.getEventDescription(d.eventNumber));

        SimpleDateFormat SQLsdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat daySdf = new SimpleDateFormat("dd.MM.YYYY");
        Calendar c = Calendar.getInstance();
        if(d.dueDate == null) {
            c.setTime(new Date());
            c.add(Calendar.DATE, expDays);
            changeList.put("§DUEDATE§", daySdf.format(c.getTime()));
        } else {
            c.setTime(d.dueDate);
            changeList.put("§DUEDATE§", daySdf.format(c.getTime()));
        }
        System.out.println("Sending message to " + d.mail + "... ");
        String message = tReader.textAdapter(changeList);
        String title = tReader.getTitle(changeList);
        int messageResult = sStarter.sendMessage(d.mail, title, message);
        if (messageResult == 0) {
            System.out.println("Message sent successfully");
            sqlManager.runSQLUpdate("UPDATE Debt SET due_date='" + SQLsdf.format(c.getTime()) +
                    "',last_mail_sent='" + SQLsdf.format(new Date()) +
                    "' WHERE reference_number=" + d.getReferenceNumber() + ";");
            //update due date and
        } else {
            System.out.println("Couldn't send message. Error code: " + messageResult);
            return -1;
        }
        messageSaver(d.name, d.mail, d.getReferenceNumber(), d.eventNumber, title, message, messageResult);
        return 0;
    }

    //This method saves the message to the computer and to the SQL Mail -table
    private int messageSaver(String name, String mailTo, int referenceNumber, int event, String title, String message, int messageResult) {
        if(!new File("mails/" + event).exists()) new File("mails/" + event).mkdirs();
        Date d = new Date();
        String path = "mails/"+event+"/"+ name + "_" + d + ".txt";
        try (PrintWriter out = new PrintWriter(path)) {
            //PrintWriter out = new PrintWriter(debtList.get(i).name + ".txt");
            out.println("Sent to mail: " + mailTo);
            out.println(message);
            out.println("EOF. Ended with a code of " + messageResult);
            out.close();
            sqlManager.runSQLUpdate("INSERT INTO Mail (mail, reference_number, subject, path, status)" +
                    "VALUES ('"+mailTo+"',"+referenceNumber+",'"+title+"','"+path+"','"+messageResult+"');");
        } catch (FileNotFoundException e) {
            System.out.println("couldn't write info of " + name);
            e.printStackTrace();
            sqlManager.runSQLUpdate("INSERT INTO Mail (mail, reference_number, subject, path, status)" +
                    "VALUES ('"+mailTo+"',"+referenceNumber+",'"+title+"','ERROR','"+messageResult+"');");
            return -200;
        }
        return 0;
    }

}
