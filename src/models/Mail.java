package models;

public class Mail {

    //public enum Status {UNKNOWN, NOT_SENT, IN_QUEUE, SENT, FAILED, CRASHED};
    //Enumit talletettu SQL-tauluun.

    private int id, debtId;
    private String sender, receiver, subject, message, status;

    public Mail(int id, int debtId, String sender, String receiver, String subject, String message, String status) {
        this(id, sender, receiver, subject, message, status);
        this.debtId = debtId;
    }
    public Mail(int id, String sender, String receiver, String subject, String message, String status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.message = message;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public int getDebtId() { return debtId; }
    public String getSender() {
        return sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getSubject() {
        return subject;
    }
    public String getMessage() {
        return message;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }
}
