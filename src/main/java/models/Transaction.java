package models;

public class Transaction {

    private int id;
    private String payer = null;
    private String archivingCode;
    private String date = null;
    private long referenceNumber;
    private String message = null;
    private int nbr;
    private int debtId;
    private int eventId;
    private String explanation;

    //These variables are used when parsing the bank statement.
    private int numberInBankStatement;
    private int sum = 0;

    public Transaction() {}

    public Transaction(int id, String payer, String archivingCode, String date, long referenceNumber, String message,
                       int nbr, int debtId, int eventId, String explanation) {
        this.id = id;
        this.payer = payer;
        this.archivingCode = archivingCode;
        this.date = date;
        this.referenceNumber = referenceNumber;
        this.message = message;
        this.nbr = nbr;
        this.debtId = debtId;
        this.eventId = eventId;
        this.explanation = explanation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public void setArchivingCode(String archivingCode) {
        this.archivingCode = archivingCode;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setReferenceNumber(long referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public void setMessage(String message) {
        try{ referenceNumber = Integer.parseInt(message); }
        catch(NumberFormatException e) { this.message = message; }
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }

    public void setDebtId(int debtId) {
        this.debtId = debtId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setNumberInBankStatement(int numberInBankStatement) {
        this.numberInBankStatement = numberInBankStatement;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public String getPayer() {
        return payer;
    }

    public String getArchivingCode() {
        return archivingCode;
    }

    public String getDate() {
        return date;
    }

    public long getReferenceNumber() {
        return referenceNumber;
    }

    public String getMessage() {
        return message;
    }

    public int getNbr() {
        return nbr;
    }

    public int getDebtId() {
        return debtId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getNumberInBankStatement() {
        return numberInBankStatement;
    }

    public int getSum() {
        return sum;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "No" + numberInBankStatement + ", date: " + date + ", rNumber: " + referenceNumber + ", message: " + message + ", payer: " + payer + ", sum: " + sum + ", eNumber: " + eventId;
    }

}
