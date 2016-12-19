import Bookkeeping.*;
import Mail.MessageCreator;
import Tools.BankStatementParser;
import Tools.ExcelWriter;
import Tools.Transaction;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            System.out.println("    2: Send new messages,");
            System.out.println("    3: ");
            System.out.println("    4: generate reference number,");
            System.out.println("    5. Chance all statuses of debts which are late to 'LATE'");
            System.out.println("    6. ");
            System.out.println("    7. ");
            System.out.println("    8: Parse RV statements from PDF and create excel out of it,");
            System.out.println("    9: Parse bank statements from PDF and create excel file out of it,");
            System.out.println("    0: Exit.");
            int userInput = getInteger();
            switch (userInput) {
                case 1:
                    createEventDebts();
                    break;
                case 2:
                    sendPaymentMails();
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


    private static int sendPaymentMails() {
        System.out.println("Please enter message text file: ");
        String file = sc.nextLine();
        MessageCreator mCreator;
        try {
            mCreator = new MessageCreator(file);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't send mails due to file reading error.");
            return -2;
        }

        System.out.println("Debts with status 1. 'NOT_SENT', 2. 'LATE', 3. 'ACCRUAL'");
        int userInput = getInteger();
        List<Debt> debts = null;
        if(userInput == 1) debts = dManager.getDebts("NOT_SENT");
        else if(userInput == 2) debts = dManager.getDebts("LATE");
        else if(userInput == 3) debts = dManager.getDebts("ACCRUAL");
        else {
            System.out.println("not a valid number, idiot.");
            return -1;
        }

        System.out.println("Any event preference? Write 0 if none.");
        userInput = getInteger();
        if(userInput != 0) {
            for(Debt d : debts) {
                if(d.eventNumber != userInput) debts.remove(d);
            }
        }

        System.out.println("NUMBER OF PAYMENTS: " + debts.size());
        mCreator.sendListOfMails(debts);
        return 0;
    }

    //do this again
    private static int sendOneMessage() {
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
            if(t.getMessage() != null && !t.getMessage().equals("Palvelumaksut") && !t.getMessage().equals("???"))
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
        try {
            int a = sc.nextInt();
            sc.nextLine();
            return a;
        } catch (Exception e) {
            System.out.println("Please enter an integer: ");
            return getInteger();
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
