import services.DebtService;
import tools.TransactionFilter;

public class Kroisos {

    public static void main(String [] args) {
        System.out.println("Hello!");
        for (String s : args) {
            TransactionFilter asd = new TransactionFilter("tiliotteet/" + Integer.parseInt(s) + ".pdf");
        }

        //System.out.println(DebtService.updateLateStatuses());

    }
}
