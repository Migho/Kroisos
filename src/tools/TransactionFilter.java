package tools;

import models.Debt;
import models.Transaction;
import services.DebtService;
import services.EntryService;
import services.EventService;
import services.TransactionService;

import java.util.ArrayList;

public class TransactionFilter {

    private DebtService dManager = new DebtService();
    private EventService eManager = new EventService();

    public TransactionFilter(String file) {
        BankStatementParser parser = new BankStatementParser(file);
        ArrayList<Transaction> list = filterTransactions(parser.getTransfers());
        //tässä välissä pitäisi tehdä manuaaliset työt.
        list = TransactionService.saveTransactions(list);
        EntryService.saveEntries(list, 6);
        //all done?
        //Pitäis viä tallentaa tiliote
    }

    private ArrayList<Transaction> filterTransactions(ArrayList<Transaction> list) {
        for(Transaction t : list) {
            Transaction transaction = t;    //täh?
            System.out.println(t);
            if(transaction.getReferenceNumber() >= 0 && transaction.getSum() >= 0) {
                if(transaction.getReferenceNumber() > 1000000 && transaction.getReferenceNumber() < 2000000) {
                    //Jäsenmaksu. Pitäis tehdä tähänki joku järkevämpi tsydeemi ettei koodista tarvi muuttaa summia :)
                    transaction.setEventId(900);    //jäsenmaksujen tapahtumaID!
                    if(t.getSum() == 400) t.setExplanation("Jäsenmaksu 1 vuosi");
                    else if(t.getSum() == 1000) t.setExplanation("Jäsenmaksu 3 vuotta");
                    else if(t.getSum() == 1500) t.setExplanation("Jäsenmaksu 5 vuotta");
                    //Lisäski salee vois myös muuttaa membersin antamien jäsenmaksujen viitenumeroavaruutta? Ehkä?
                    //Toimii toki vissiin näilläki viitenumeroilla.
                } else if(transaction.getReferenceNumber() >= 2000000) {
                    System.out.println("Tapahtuma!");
                    //Tapahtumamaksu
                    t.setEventId(dManager.getDebt(t.getReferenceNumber()).getEventId());
                    //t.setEventId((int)transaction.getReferenceNumber()/10000);
                    Debt debt = dManager.getDebt(t.getReferenceNumber());
                    if(debt != null) {
                        dManager.markDebtPaid(transaction.getReferenceNumber(), transaction.getSum());
                        String eventDescription = eManager.getEventDescription(t.getEventId());
                        if(!eventDescription.isEmpty()) t.setExplanation(eventDescription + ", " + debt.getUser().getName());
                        t.setDebtId(debt.getId());
                    } else {
                        //if no payment is found with this reference number, mb you should do something?
                    }
                } else {}
            }
            System.out.println("after: " + t);
        }

        return list;
    }

}
