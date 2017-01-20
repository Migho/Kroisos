package mail;

import models.Debt;
import services.DebtService;
import services.EventService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageCreator {

    private int expDays = 14;
    private MailSender mSender;
    private EventService eManager = new EventService();
    private String subject;
    private String message;
    
    public MessageCreator(String subject, String text, MailSender mSender) {
        this.subject = subject;
        this.message = text;
        this.mSender = mSender;
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
        List<Debt> unsuccessfulMails = new ArrayList<>();
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
     * @param d Debt used for the information in the mail
     * @return 0 if successful, < 0 if failed.
     */
    private int formAndSendMail(Debt d) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //replace this information from text
        HashMap<String, String> changeList = new HashMap<>();
        changeList.put("§REFERENCENUMBER§", Integer.toString(d.getReferenceNumber()));
        changeList.put("§NAME§", d.getUser().getName());
        changeList.put("§SUM§", String.format("%.2f", d.getSum()));
        changeList.put("§EVENTNAME§", eManager.getEventName(d.getEventId()));
        changeList.put("§EVENTDESC§", eManager.getEventDescription(d.getEventId()));
        changeList.put("§INFO§", d.getInfo());
        SimpleDateFormat daySdf = new SimpleDateFormat("dd.MM.YYYY");
        Calendar c = Calendar.getInstance();
        if(d.getDueDate() == null) {  //if debt has no due date, it will be set.
            c.setTime(new Date());
            c.add(Calendar.DATE, expDays);
            d.setDueDate(c.getTime());
        } else c.setTime(d.getDueDate());
        changeList.put("§DUEDATE§", daySdf.format(c.getTime()));

        //starting message sending at this point
        int messageResult = mSender.sendEmail(d.getUser().getMail(), subjectAdapter(changeList), messageAdapter(changeList));
        if (messageResult == 0) {
            DebtService.updateDebt(d);
            //update due date
        } else {
            return -1;
        }
        messageSaver(d.getUser().getName(), d.getUser().getMail(), d.getReferenceNumber(), d.getEventId(), subjectAdapter(changeList), messageAdapter(changeList), messageResult);
        return 0;
    }

    private String subjectAdapter(HashMap<String, String> list) { return textAdapter(list, subject); }
    private String messageAdapter(HashMap<String, String> list) { return textAdapter(list, message); }

    /**
     * This method will replace all HashMap keys matches from the text with HashMap values.
     * @param list If a key is found from the text, it will be replaces with its value.
     * @return Returns modified text.
     */
    private String textAdapter(HashMap<String, String> list, String text) {
        String modifiedText = message;
        for(Map.Entry<String, String> entry : list.entrySet()) {
            modifiedText = modifiedText.replaceAll(entry.getKey(), entry.getValue());
        }
        return modifiedText;
    }

    //This method saves the message to the computer
    private int messageSaver(String name, String mailTo, int referenceNumber, int event, String title, String message, int messageResult) {
        if(!new File("mails/" + event).exists()) new File("mails/" + event).mkdirs();
        Date d = new Date();
        String path = "mails/"+event+"/"+ name + "_" + d + ".txt";
        try (PrintWriter out = new PrintWriter(path)) {
            //PrintWriter out = new PrintWriter(debtList.get(i).name + ".txt");
            out.println("Sent to mail: " + mailTo);
            System.out.println("Title: " + title);
            out.println(message);
            out.println("EOF. Ended with a code of " + messageResult);
            out.close();
        } catch (FileNotFoundException e) {
            return -200;
        }
        return 0;
    }

}
