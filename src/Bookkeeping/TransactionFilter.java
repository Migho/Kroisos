package Bookkeeping;

import Tools.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class TransactionFilter {

    private Scanner sc;
    private DebtManager dManager;
    private EventManager eManager = new EventManager();

    public TransactionFilter(Scanner sc, DebtManager dManager) {
        this.sc = sc;
        this.dManager = dManager;
    }

    public ArrayList<Transaction> filterTransactions(ArrayList<Transaction> list) {
        for(int i=0; i<list.size(); i++) {
            Transaction transaction = list.get(i);
            if(transaction.getReferenceNumber() >= 0 && transaction.getSum() >= 0) {
                if(transaction.getReferenceNumber() > 1000000 && transaction.getReferenceNumber() < 10000000) {
                    //Jäsenmaksu
                    list.get(i).type = "Jäsenmaksut";
                } else if(transaction.getReferenceNumber() >= 10000000) {
                    //Tapahtumamaksu
                    Debt temp = dManager.getDebt(list.get(i).getReferenceNumber());
                    if(temp != null) list.get(i).name = temp.name.toUpperCase();
                    if(dManager.markDebtPaid(transaction.getReferenceNumber(), transaction.getSum())==0)
                        System.out.println("No debt found");
                    list.get(i).type = eManager.getType((int)transaction.getReferenceNumber()/10000);
                    String eventDescription = eManager.getEventDescription((int)transaction.getReferenceNumber()/10000);
                    if(!eventDescription.isEmpty()) list.get(i).setMessage(eventDescription);
                }
            } if(transaction.type.equals("Undefined")) {
                list.get(i).type = askType(list.get(i).toString());
            }

            if(list.get(i).type.equals("Jäsenmaksut")) {
                if(list.get(i).getSum() > 3.995 && list.get(i).getSum() < 4.005) list.get(i).setMessage("Jäsenmaksu 1 vuosi");
                else if(list.get(i).getSum() > 9.995 && list.get(i).getSum() < 10.005) list.get(i).setMessage("Jäsenmaksu 3 vuotta");
                else if(list.get(i).getSum() > 14.995 && list.get(i).getSum() < 15.005) list.get(i).setMessage("Jäsenmaksu 5 vuotta");
            }
        }

        return list;
    }

    private String askType(String info) {
        //väliaikainen ratkaisu; kehitellään sitte webbikäliin parempi
        System.out.println("Unknown transaction: " + info);
        System.out.println("4.  Saadut ennakot                  25. Muu huvitoiminta");
        System.out.println("5.  Maksetut ennakot                26. Jäsenmaksut");
        System.out.println("6.  Siirtovelat                     27. TKO-älyn tuotteet");
        System.out.println("7.  Siirtosaamiset                  28. Muu varainhankinta");
        System.out.println("8.  Rahansiirrot ja täsmäykset      29. Lehtiavustus");
        System.out.println("9.  Ruokavälityksen varastot        30. HYY:n toiminta-avustus");
        System.out.println("10. TKO-äly-tuotteiden varasto      31. Haalarimainokset");
        System.out.println("11. Kulttuuritoiminta               32. Accenturen yhteistyösopimus");
        System.out.println("12. Liikuntatoiminta                33. Yhteistyösopimukset");
        System.out.println("13. Ruokavälitys                    34. Apurahat");
        System.out.println("14. README                          35. Korkotuotot");
        System.out.println("15. Risteilyt                       36. Fuksitoiminta");
        System.out.println("16. Saunaillat                      37. Lehtitilaukset");
        System.out.println("17. Sitsit                          38. Muu jäsentoiminta");
        System.out.println("18. Vuosijuhla                      39. Laitehankinnat");
        System.out.println("19. Kerhotoiminta                   40. Muut hankinnat");
        System.out.println("20. Teemaillat                      41. Edustus");
        System.out.println("21. Kalustehankinnat                42. Hallinto");
        System.out.println("22. Koulutus                        43. Palvelumaksut");
        System.out.println("23. Muu yhdistystoiminta            44. Tiedotus");
        System.out.println("24. ATKYTP                          blank: Undefined");

        String s = sc.nextLine();
        while(true) {
            try {
                Integer.parseInt(s);
                ResultSet rs = SQLManager.runSQLQuery("SELECT name FROM Account WHERE id=?;", new Object[] {s});
                try {
                    if(rs.next()) {
                        return rs.getString("name");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return "Undefined";
                }
            } catch (NumberFormatException e) {}
            if(s.equals("")) return "Undefined";
            System.out.print("Please type a valid number: ");
            s = sc.nextLine();
        }
    }

}
