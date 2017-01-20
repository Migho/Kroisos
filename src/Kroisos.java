<<<<<<< HEAD
import models.Debt;
import services.DebtService;
=======
import Bookkeeping.DebtManager;
import Bookkeeping.EventManager;
>>>>>>> ec38dc1cc4dde909f971ad635f548a84db2481b6

public class Kroisos {

    public static void main(String [] args) {
<<<<<<< HEAD
        for(Debt d : DebtService.getDebts()) {
            if(d.getUser()==null) System.out.println("Debt user is null!");
            else System.out.println(d.getUser());
        }
        //new TextUserInterface();
=======
        new TextUserInterface();
>>>>>>> ec38dc1cc4dde909f971ad635f548a84db2481b6
    }
}
