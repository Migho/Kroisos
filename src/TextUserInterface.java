import Bookkeeping.*;
import Mail.MessageCreator;
import Tools.BankStatementParser;
import Tools.ExcelWriter;
import Tools.Transaction;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by migho on 29.9.2016.
 */
public class TextUserInterface {
    private static DebtManager dManager;
    private static Scanner sc = new Scanner(System.in);

    public TextUserInterface() {
        dManager = new DebtManager();
        sc = new Scanner(System.in);
        boolean exit = false;
        while(!exit) {
            System.out.println("Enter number of your choice: ");
            System.out.println("    1: Create new debts with event number and emails,");
            System.out.println("    2: Send new payment instructions with event number,");
            System.out.println("    3: Send message with reference number,");
            System.out.println("    4: generate reference number,");
            System.out.println("    5. Chance all statuses of debts that are late to 'LATE'");
            System.out.println("    6. Send payment reminders,");
            System.out.println("    7. null,");
            System.out.println("    8: Parse RV statements from PDF and create excel out of it,");
            System.out.println("    9: Parse bank statements from PDF and create excel file out of it,");
            System.out.println("    0: Exit.");
            int userInput = getInteger();
            switch (userInput) {
                case 1:
                    createEventDebts();
                    break;
                case 2:
                    sendPaymentInstructions();
                    break;
                case 3:
                    sendOneMessage();
                    break;
                case 4:
                    generateReferenceNumber();
                    break;
                case 5:
                    updateStatuses();
                    break;
                case 6:
                    sendPaymentReminders();
                    break;
                case 7:
                    break;
                case 8:
                    parseRvPDF();
                    break;
                case 9:
                    parsePDF();
                    break;
                case 0:
                    exit = true;
                    break;
            }
        }
        System.out.println("Thxbye!");
    }

    private static int createEventDebts() {
        System.out.println("Please give mails separated by commas: ");
        String mails = sc.nextLine();
        System.out.println("Please give names separated by commas: ");
        String names = sc.nextLine();
        System.out.println("Please give event number: ");
        int eventNumber = getInteger();
        System.out.println("Please give the price of the event: ");
        double price = sc.nextDouble();

        int result = dManager.CreateEventDebts(mails.split(", "), names.replace("\"", "").split(", "), eventNumber, price);
        if(result!=0) System.out.println("WARNING: DEBT CREATION RESULT " + result);
        result = dManager.writeDebts();
        if(result==0) System.out.println("Write successful");
        else System.out.println("An error occurred while writing the debts!");
        return 0;
    }

    private static int sendPaymentInstructions() {
        System.out.println("Please give event number: ");
        int eventNumber = getInteger();
        System.out.println("Please enter message text file: ");
        String file = sc.nextLine();
        MessageCreator mCreator;
        //if changing expiration day is necessary, use mCreator.setExpirationDay. Default is 14 days.
        try {
            mCreator = new MessageCreator(file);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't send mails due to file reading error.");
            return -2;
        }
        List<Debt> debtsNotSend = dManager.getAllEventDebts(eventNumber);
        debtsNotSend = mCreator.sendPaymentInstructions(debtsNotSend);
        for(int i=0; debtsNotSend.size() != 0 && i<3; i++) {
            System.out.println("Couldn't send " + debtsNotSend.size() + " mail(s). Gonna retry now");
            debtsNotSend = mCreator.sendPaymentInstructions(debtsNotSend);
        }
        if(debtsNotSend.size() != 0) System.out.println(debtsNotSend.size() + " MAIL(S) FAILED PERMANENTLY !");
        return 0;
    }

    private static int sendPaymentReminders() {
        System.out.println("Please enter message text file: ");
        String file = sc.nextLine();
        MessageCreator mCreator;
        try {
            mCreator = new MessageCreator(file);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't send mails due to file reading error.");
            return -2;
        }
        List<Debt> debtsNotSend = dManager.getDebts();
        //System.out.println("NUMBER OF PAYMENTS: " + debtsNotSend.size());
        debtsNotSend = mCreator.sendPaymentReminder(debtsNotSend);
        for(int i=0; debtsNotSend.size() != 0 && i<3; i++) {
            System.out.println("Couldn't send " + debtsNotSend.size() + " mail(s). Gonna retry now");
            debtsNotSend = mCreator.sendPaymentReminder(debtsNotSend);
        }
        if(debtsNotSend.size() != 0) System.out.println(debtsNotSend.size() + " MAIL(S) FAILED PERMANENTLY !");
        return 0;
    }

    //do this again
    private static int sendOneMessage() {
        System.out.println("Please give reference number: ");
        int referenceNumber = getInteger();
        Debt temp = dManager.getDebt(referenceNumber);
        if(temp.getReferenceNumber() != referenceNumber) {
            System.out.println("No such reference number...");
            return -1;
        }
        List<Debt> a = new ArrayList<>();
        a.add(temp);
        System.out.println("Please enter message text file: ");
        String file = sc.nextLine();
        MessageCreator mCreator;
        try {
            mCreator = new MessageCreator(file);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't send mails due to file reading error.");
            return -2;
        }
        a = mCreator.sendPaymentInstructions(a);
        if(a.size()!=0) {
            System.out.println("ERROR: Couldn't send mail.");
            return -3;
        }
        return 0;
    }

    private static int updateStatuses() {
        BookkeepFixer bFixer = new BookkeepFixer();
        System.out.println("About to update " + bFixer.updateDebtStatus(false) + " status(es). Continue? y/n ");
        if(getUserIput()) bFixer.updateDebtStatus(true);
        return 0;
    }

    private static int parseRvPDF() {
        System.out.println("Please enter PDF file name: ");
        String file = sc.nextLine();
        BankStatementParser parser = new BankStatementParser(file);
        ExcelWriter excelWriter = new ExcelWriter();
        ArrayList<Transaction> list = parser.getTransfers();
        for(Transaction t : list) {
            if(!t.getMessage().equals("Palvelumaksut") && !t.getMessage().equals("???"))
                t.setMessage("Ruokavälityksen tuotot");
        }
        excelWriter.save(list);
        System.out.println("Done.");
        return 0;
    }

    private static int parsePDF() {
        System.out.println("Please enter PDF file name: ");
        String file = sc.nextLine();
        BankStatementParser parser = new BankStatementParser(file);
        TransactionFilter filter = new TransactionFilter(sc, dManager);
        ExcelWriter excelWriter = new ExcelWriter();
        System.out.println("Will now start parsing the transactons...");
        excelWriter.save(filter.filterTransactions(parser.getTransfers()));
        dManager.writeDebts();
        System.out.println("Done.");
        return 0;
    }

    private static int generateReferenceNumber() {
        System.out.println("Please give event number: ");
        int eventNumber = getInteger();
        System.out.println("Please give participant number: ");
        int participantNumber = getInteger();
        ReferenceNumberGenerator r = new ReferenceNumberGenerator(eventNumber);
        r.setParticipantNumber(participantNumber);
        System.out.println("Your reference number is " + r.getReferenceNumber());
        return 0;
    }

    private static int getInteger() {
        while(true) {
            try {
                int a = sc.nextInt();
                sc.nextLine();
                return a;
            } catch (Exception e) {
                System.out.println("Please enter an integer: ");
            }
        }
    }

    private static boolean getUserIput() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String s = scanner.nextLine();
            if(s.equals("y")) return true;
            else if(s.equals("n")) return false;
            System.out.println("Please write \"y\" or \"n\". ");
        }
    }
}
