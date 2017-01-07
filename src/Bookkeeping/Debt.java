package Bookkeeping;

import java.util.Date;

public class Debt implements Comparable<Debt> {
    private int eventNumber = 0;
    private int participantNumber = 0;
    private int checkDigit = 0;
    private String name = "";
    private String mail = "";
    private Double sum = 0.0; //pyöristysvirheitä ei pitäisi tapahtua mutta varmaan silti järkevää muuttaa integeriksi?
    private String info = "";
    private Date dueDate;

    public int getEventNumber() {
        return eventNumber;
    }
    public int getParticipantNumber() {
        return participantNumber;
    }
    public int getCheckDigit() {
        return checkDigit;
    }
    public String getName() {
        return name;
    }
    public String getMail() {
        return mail;
    }
    public Double getSum() {
        return sum;
    }
    public String getInfo() {
        return info;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setEventNumber(int eventNumber) {
        this.eventNumber = eventNumber;
    }
    public void setParticipantNumber(int participantNumber) {
        this.participantNumber = participantNumber;
    }
    public void setCheckDigit(int checkDigit) {
        this.checkDigit = checkDigit;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setSum(Double sum) {
        this.sum = sum;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getReferenceNumber() {
        return eventNumber*10000 + participantNumber*10 + checkDigit;
    }

    public int setReferenceNumber(int eventNumber, int participantNumber, int checkDigit) {
        if(eventNumber > 9999 || participantNumber > 999 || checkDigit > 9) return -1;
        this.eventNumber = eventNumber;
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

    @Override
    public int compareTo(Debt d) {
        return this.getReferenceNumber() - d.getReferenceNumber();
    }
}
