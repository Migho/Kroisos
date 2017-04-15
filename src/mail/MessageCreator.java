package mail;

import models.Debt;
import models.Mail;
import services.DebtService;
import services.EventService;
import services.MailService;

import java.text.SimpleDateFormat;
import java.util.*;

public class MessageCreator {

    private int expDays = 14;
    private EventService eManager = new EventService();
    private String subject;
    private String message;
    private String sendUser;
    
    public MessageCreator(String subject, String text, String sendUser) {
        this.subject = subject;
        this.message = text;
        this.sendUser = sendUser;
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
     *
     * @param d
     * @return
     */
    public int createMail(Debt d) {
        HashMap<String, String> changeList = new HashMap<>();
        changeList.put("§REFERENCENUMBER§", Integer.toString(d.getReferenceNumber()));
        changeList.put("§NAME§", d.getUser().getName());
        changeList.put("§SUM§", String.format("%.2f", (double)d.getSum()/100));
        changeList.put("§EVENTNAME§", eManager.getEventName(d.getEventId()));
        changeList.put("§EVENTDESC§", eManager.getEventDescription(d.getEventId()));
        changeList.put("§INFO§", d.getInfo());
        SimpleDateFormat daySdf = new SimpleDateFormat("dd.MM.YYYY");
        Calendar c = Calendar.getInstance();
        if(d.getDueDate() == null) {  //if debt has no due date, it will be set.
            c.setTime(new Date());
            c.add(Calendar.DATE, expDays);
            d.setDueDate(c.getTime());
            DebtService.updateDebt(d);  //update due date to the debt
        } else c.setTime(d.getDueDate());
        changeList.put("§DUEDATE§", daySdf.format(c.getTime()));
        return MailService.addMail(new Mail(0, d.getId(), sendUser, d.getUser().getMail(),
                subjectAdapter(changeList), messageAdapter(changeList), "NOT_SENT"));
    }

    private String subjectAdapter(HashMap<String, String> list) { return textAdapter(list, subject); }
    private String messageAdapter(HashMap<String, String> list) { return textAdapter(list, message); }

    /**
     * This method will replace all HashMap keys matches from the text with HashMap values.
     * @param list If a key is found from the text, it will be replaces with its value.
     * @return Returns modified text.
     */
    private String textAdapter(HashMap<String, String> list, String text) {
        String modifiedText = text;
        for(Map.Entry<String, String> entry : list.entrySet())
            modifiedText = modifiedText.replaceAll(entry.getKey(), entry.getValue());
        return modifiedText;
    }

}
