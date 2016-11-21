package Tools;


/**
 *
 */
public class Transaction {

    public int number;
    public String date = null;
    public String name = null;
    private double sum = 0;
    private String message = null;
    public String type = "Undefined";

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
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

}
