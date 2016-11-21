package Bookkeeping;

import java.util.Date;

/**
 * Created by migho on 27.9.2016.
 */
public class Debt implements Comparable<Object>{
    public int eventNumber = 0;
    public int participantNumber = 0;
    public int checkDigit = 0;
    public String name = "";
    public String mail = "";
    public Double sum = 0.0;
    public String externalInfo = "";
    public Date dueDate;

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
    public int compareTo(Object object) {
        if (object != null && object instanceof Debt)
            return this.getReferenceNumber() - ((Debt) object).getReferenceNumber();
        return -1;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof Debt)
            return this.getReferenceNumber() == ((Debt) object).getReferenceNumber();
        return false;
    }
}
