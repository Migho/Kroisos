package mail;

import models.Mail;
import database.MailDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by migho on 18.2.2017.
 */
public class Sender extends Thread {

    private MailAgent mAgent = new MailAgent();
    private int sleepTime;
    private int eventNumber = -1;
    private String status;

    /**
     * @param host Connection host
     * @param port Port to use
     * @param userName Username
     * @param mailAddress Mail address (first part before @)
     * @param domain Domain of the mail address (@example.com)
     * @param password Account password
     */
    public Sender(String host, String port, String userName, String mailAddress, String domain,
                  String password, int sleepTime) {
        mAgent.initializeConnection(host, port, userName, mailAddress, domain, password);
        this.sleepTime = sleepTime;
        this.eventNumber = eventNumber;
        this.status = status;
    }

    public void setTarget(int eventNumber, String status) {
        this.eventNumber = eventNumber;
        this.status = status;
    }

    /**
     * Start shooting the mails :)
     */
    public void run() {
        if(eventNumber == -1 || status == null) return;
        sendListOfMails(MailDAO.getMails(eventNumber, status), 3);
    }

    /**
     * Send list of mails.
     * @param mails List of mails.
     * @param amountOfRetries How many times to retry failed mails.
     * @return Will return a list of failed debts.
     */
    private List<Mail> sendListOfMails(List<Mail> mails, int amountOfRetries) {
        if(amountOfRetries<=0) return mails;
        List<Mail> unsuccessfulMails = new ArrayList<>();
        for(Mail m : mails) {
            if (mAgent.sendEmail(m.getReceiver(), m.getSubject(), m.getMessage()) < 0) {
                //Sending the mail failed!
                m.setStatus("FAILED");
                unsuccessfulMails.add(m);
            } else {
                //Sending the mail was successful!
                m.setStatus("SENT");
            }
            //update mail status
            MailDAO.updateMail(m);
            //sleep some time in order to prevent possible spam filter triggering
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sendListOfMails(unsuccessfulMails, amountOfRetries-1);
    }

}
