import services.DebtService;
import tools.TransactionFilter;

public class ParserMain {

    public static void main(String [] args) {
        System.out.println("Hello!");
        for (String s : args) {
            TransactionFilter asd = new TransactionFilter("tiliotteet/" + Integer.parseInt(s) + ".pdf");
        }

        /* If status is pending and due date is already gone, update the status to LATE */
        //System.out.println(DebtService.updateLateStatuses());

    }
}
