package models;

/**
 * Created by migho on 20.3.2017.
 */
public class Entry {

    private int id;
    private int accountId;
    private int sum;
    private int transactionId;
    private boolean debit;

    public Entry(int id, int accountId, int sum, int transactionId, boolean debit) {
        this.id = id;
        this.accountId = accountId;
        this.sum = sum;
        this.transactionId = transactionId;
        this.debit = debit;
    }

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getSum() {
        return sum;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public boolean isDebit() {
        return debit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setDebit(boolean debit) {
        this.debit = debit;
    }
}
