package tools;

/**
 * This class will generate a set of valid reference numbers.
 */
public class ReferenceNumberGenerator {

    private int eventNumber = 0;
    private int participantNumber;
    private int checkDigit = 7;

    public ReferenceNumberGenerator(int eventNumber) {
        this.eventNumber = eventNumber;
        participantNumber = 0;
        nextReferenceNumber();
    }

    /**
     * Returns Reference number of type XXXXYYYR:
     *      XXXX = event number
     *      YYY = ParticipantNumber
     *      R = Check digit
     * @return Reference number as int.
     */
    public int getReferenceNumber() {
        return eventNumber*10000 + participantNumber*10 + checkDigit;
    }

    public int getEventNumber() { return eventNumber; }
    public int getParticipantNumber() { return participantNumber; }
    public int getCheckDigit() { return checkDigit; }

    /**
     * Will generate next reference number.
     */
    public void nextReferenceNumber() {
        participantNumber++;
        if(participantNumber >= 1000) {
            System.out.println("PARTICIPANT NUMBER IS 001 AGAIN");
            participantNumber = 1;
        }
        generateCheckDigit();
    }

    public void setParticipantNumber(int number) {
        participantNumber = number;
        generateCheckDigit();
    }

    private void generateCheckDigit() {
        checkDigit = 9 - (7*(eventNumber/1000) +
                1*(eventNumber/100-(eventNumber/1000)*10) +
                3*(eventNumber/10 - (eventNumber/100-(eventNumber/1000)*10)*10 - (eventNumber/1000)*100) +
                7*(eventNumber-(eventNumber/10)*10) +
                1*(participantNumber/100) +
                3*(participantNumber/10-(participantNumber/100)*10) +
                7*(participantNumber-(participantNumber/10)*10)
        -1)%10;
        if(checkDigit==10) checkDigit = 0;
    }
}
