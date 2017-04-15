package models;

import java.util.Date;

public class Debt implements Comparable<Debt> {
    private int id;
    private int eventId = 0;
    private int fakeEventId = 0; //fast fix; sometimes event number can start with something else than eventId.
    private int participantNumber = 0;
    private int checkDigit = 0;
    private User user;
    private int sum = 0;
    private String info = "";
    private Date dueDate;
    private String status;
    private Date lastMailSent;
    private int penalty;

    public Date getLastMailSent() {
        return lastMailSent;
    }
    public void setLastMailSent(Date lastMailSent) {
        this.lastMailSent = lastMailSent;
    }
    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }
    public int getPenalty() {
        return penalty;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }
    public int getParticipantNumber() {
        return participantNumber;
    }
    public int getCheckDigit() {
        return checkDigit;
    }
    public User getUser() {
        return user;
    }
    public int getSum() {
        return sum;
    }
    public String getInfo() {
        return info;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public String getStatus() { return status; }
    public void setEventId(int eventId) {this.eventId = eventId;
    }
    public void setParticipantNumber(int participantNumber) {
        this.participantNumber = participantNumber;
    }
    public void setCheckDigit(int checkDigit) {
        this.checkDigit = checkDigit;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setSum(int sum) {
        this.sum = sum;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public void setStatus(String status) { this.status = status; }

    public int getReferenceNumber() {
        if(fakeEventId == 0) return eventId *10000 + participantNumber*10 + checkDigit;
        return fakeEventId *10000 + participantNumber*10 + checkDigit;
    }

    public int setReferenceNumber(int eventNumber, int participantNumber, int checkDigit) {
        if(eventNumber > 9999 || participantNumber > 999 || checkDigit > 9) return -1;
        if(eventId == 0) this.eventId = eventNumber;
        else fakeEventId = eventNumber;
        this.participantNumber = participantNumber;
        this.checkDigit = checkDigit;
        return 0;
    }

    public int setReferenceNumber(long referenceNumber) {
        int eventNumber = (int)referenceNumber/10000;
        int participantNumber = (int)(referenceNumber/10-eventNumber*1000);
        int checkDigit = (int)referenceNumber % 10;
        return this.setReferenceNumber(eventNumber, participantNumber, checkDigit);
    }

    //for Vaadin
    public String getUserName() {
        if(user != null) return user.getUsername();
        return null;
    }

    @Override
    public int compareTo(Debt d) {
        return this.getReferenceNumber() - d.getReferenceNumber();
    }
}
