package Tools;


/**
 *
 */
public class Transaction {

    private int number;
    private String date = null;
    private String name = null;
    private double sum = 0;
    private String message = null;
    private String type = "Undefined";

    public int getNumber() {
        return number;
    }
    public String getDate() {
        return date;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSum(double sum) {
        this.sum = sum;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "No" + number + ": " + date + " " + message + " " + name + " " + sum;
    }

    public void setMessage(String message) { this.message = message; }
    public void setMessage(long referenceNumber) { this.message = referenceNumber + ""; }
    public String getMessage() { return message; }
    public long getReferenceNumber() {
        if(isInteger(message)) return Long.parseLong(message);
        return -1;
    }

    public void setSum(String sum) {
        if(sum.contains("-")) {
            this.sum = -Double.parseDouble(sum.replace("-", "").replace(",", "."));
        } else {
            this.sum = Double.parseDouble(sum.replace("+", "").replace(",", "."));
        }
    }
    public void setSum(int sum) { this.sum = sum; }
    public double getSum() { return sum; }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

}
