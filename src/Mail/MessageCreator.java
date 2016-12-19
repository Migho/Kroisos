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
     * Send list of mails, and retry each failed ones 2 more times.
     * @param debts List of mails.
     * @return Will return a list of failed debts.
     */
    public List<Debt> sendListOfMails(List<Debt> debts) { return sendListOfMails(debts, 3); }

    /**
     * Send list of mails.
     * @param debts List of mails.
     * @param amountOfRetries How many times to retry failed mails.
     * @return Will return a list of failed debts.
     */
    public List<Debt> sendListOfMails(List<Debt> debts, int amountOfRetries) {
        if(amountOfRetries<=0) return debts;
        if(tReader == null) return null;
        List<Debt> unsuccessfulMails = new ArrayList<>();
        sStarter = new SessionStarter(1);             //set 0 to disable preview
        for(Debt d : debts) if(formAndSendMail(d) < 0) unsuccessfulMails.add(d);
        if(unsuccessfulMails.size()>0) {
            if(amountOfRetries==1) {
                System.out.println(unsuccessfulMails + " MAILS FAILED PERMANENTLY!");
                return unsuccessfulMails;
            }
            System.out.println("Couldn't send " + unsuccessfulMails.size() + " mail(s). Gonna retry now");
            sendListOfMails(unsuccessfulMails, amountOfRetries-1);
        }
        return debts;
    }

    /**
     * This method will take care of forming and sending one message, created from the debt.
     * @param debt Debt used for the information in the mail
     * @return 0 if successful, < 0 if failed.
     */
    private int formAndSendMail(Debt debt) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //replace this information from text
        HashMap<String, String> changeList = new HashMap<>();
        changeList.put("§REFERENCENUMBER§", Integer.toString(debt.getReferenceNumber()));
        changeList.put("§NAME§", debt.name);
        changeList.put("§SUM§", String.format("%.2f", debt.sum));
        changeList.put("§EVENTNAME§", eManager.getEventName(debt.eventNumber));
        changeList.put("§EVENTDESC§", eManager.getEventDescription(debt.eventNumber));
        changeList.put("§INFO§", debt.info);
        SimpleDateFormat daySdf = new SimpleDateFormat("dd.MM.YYYY");
        Calendar c = Calendar.getInstance();
        if(debt.dueDate == null) {  //if debt has no due date, it will be set.
            c.setTime(new Date());
            c.add(Calendar.DATE, expDays);
        } else c.setTime(debt.dueDate);
        changeList.put("§DUEDATE§", daySdf.format(c.getTime()));

        //starting message sending at this point
        System.out.println("Sending message to " + debt.mail + "... ");
        String message = tReader.textAdapter(changeList);
        String title = tReader.getTitle(changeList);
        int messageResult = sStarter.sendMessage(debt.mail, title, message);
        if (messageResult == 0) {
            System.out.println("Message sent successfully");
            sqlManager.runSQLUpdate("UPDATE Debt SET due_date=?, last_mail_sent=? WHERE reference_number=?", new Object[] {c.getTime(), new Date(), debt.getReferenceNumber()});
            //update due date and
        } else {
            System.out.println("Couldn't send message. Error code: " + messageResult);
            return -1;
        }
        messageSaver(debt.name, debt.mail, debt.getReferenceNumber(), debt.eventNumber, title, message, messageResult);
        return 0;
    }

    //This method saves the message to the computer and stores information of the mail to the Mail SQL-table.
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
                    "VALUES (?, ?, ?, ?, ?)", new Object[] {mailTo, referenceNumber, title, path, messageResult});
        } catch (FileNotFoundException e) {
            System.out.println("couldn't write info of " + name);
            e.printStackTrace();
            sqlManager.runSQLUpdate("INSERT INTO Mail (mail, reference_number, subject, path, status)" +
                    "VALUES (?, ?, ?, ?, ?)", new Object[] {mailTo, referenceNumber, title, "ERROR", messageResult});
            return -200;
        }
        return 0;
    }

}
