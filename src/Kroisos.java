import models.Debt;
import services.DebtService;

public class Kroisos {

    public static void main(String [] args) {
        for(Debt d : DebtService.getDebts()) {
            if(d.getUser()==null) System.out.println("Debt user is null!");
            else System.out.println(d.getUser());
        }
        //new TextUserInterface();
    }
}
