package Tools;

import java.util.ArrayList;
import java.util.Scanner;

//harvinaisen paskaa koodia, pitäisi varmaan koko roska koodata uudestaan. Toimii kohtalaisesti.
public class BankStatementParser {

    private enum lineType {
        DAYCHANGE, TRANSACTION, ARCHIVE_NUMBER, IBAN_INFORMATION, PAGECHANGE, UNKNOWN
    }

    private PDFReader pdfReader;
    private boolean firstPage = true;
    private String date = "00.00.00";

    public BankStatementParser(String file) {
        pdfReader = new PDFReader(file);
    }

    public ArrayList<Transaction> getTransfers() {
        ArrayList<Transaction> list = new ArrayList();
        Scanner scanner = new Scanner(getLinesWithoutPageStuff());
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            //System.out.println(line + " = " + getLineType(line, date));

            switch (getLineType(line, date)){
                case DAYCHANGE:
                    date = line.substring(13, 21);
                    break;
                case IBAN_INFORMATION:
                    break;
                case ARCHIVE_NUMBER:    //skipping one is required?
                    if(scanner.hasNextLine()) scanner.nextLine();
                    break;
                case TRANSACTION:
                    //System.out.println(line + "     TRANSACTIONLINE");
                    Transaction transaction = parseTransactionLine(line);
                    transaction.setDate(date);
                    if(scanner.hasNextLine()) line = scanner.nextLine();   //skip line "TILISIIRTO"
                    //System.out.println(line + "     SKIPPED");
                    if(line.length()>4 && line.substring(5).equals("KORTTIOSTO")) {
                        transaction.setMessage("???");
                    } else if(line.length()>=10 && line.substring(line.length()-10).equals("TILISIIRTO")) {
                        line = "";
                        String message;
                        while (scanner.hasNextLine() && !(message = scanner.nextLine()).equals("ARKISTOINTITUNNUS")
                                && !message.equals("MAKSAJAN VIITE") && !message.equals("IBAN")) {
                            //System.out.println(line + "     MESSAGE");
                            line += message;
                        }
                        transaction.setMessage(line);
                    } else if(line.length()>5 && line.substring(5).equals("PALVELUMAKSU")) {
                        transaction.setMessage("Pankkimaksut");
                    } else System.out.println("UNDEFINED TRANSACTION METHOD");
                    list.add(transaction);
                    System.out.println(transaction);
                    break;
                case UNKNOWN:
                    break;
            }
        }
        pdfReader.closePDF();
        return list;
    }
    /**
     * Deals directly with the pdfReader, and will delete useless headers and page change texts. Works 100%.
     * @return String without headers.
     */
    private String getLinesWithoutPageStuff() {
        String text = "";
        String line;
        if(pdfReader.hasNextPage()) {
            Scanner scanner = new Scanner(pdfReader.getNextPage());
            for(int i=0; i<28; i++) if(scanner.hasNextLine()) scanner.nextLine();       //skip first page header
            //if(scanner.hasNextLine()) date = scanner.nextLine().substring(13, 21);
            while(scanner.hasNextLine()) if(!(line = scanner.nextLine()).equals("* JATKUU *")) text += line + "\n";
            while(pdfReader.hasNextPage()) {
                scanner = new Scanner(pdfReader.getNextPage());
                for(int i=0; i<16; i++) if(scanner.hasNextLine()) scanner.nextLine();   //rest headers are shorter.
                while(scanner.hasNextLine())  if(!(line = scanner.nextLine()).equals("* JATKUU *")) text += line + "\n";
            }
        }
        return text;
    }

    private lineType getLineType(String line, String date) {
        if(line.contains("KIRJAUSPÄIVÄ")) return lineType.DAYCHANGE;
        if(line.equals("IBAN")) return lineType.IBAN_INFORMATION;
        if(line.equals("ARKISTOINTITUNNUS")) return lineType.ARCHIVE_NUMBER;
        //if(line.contains("TILISIIRTO")) return lineType.ARCHIVE_NUMBER;

        //saattaa vaatia hiomista!
        String formatedDate = "";
        formatedDate += date.substring(6, 8);
        formatedDate += date.substring(3, 5);
        formatedDate += date.substring(0, 2);
        if(line.length() > 20 && line.substring(0, 6).equals(formatedDate)) return lineType.TRANSACTION;
        return lineType.UNKNOWN;
    }

    private Transaction parseTransactionLine(String line) {
        line = line.substring(18);
        if(line.substring(0, 3).equals(" A ")) line = line.substring(3);
        line = line.substring(5);
        Transaction t = new Transaction();
        t.setNumber(Integer.parseInt(line.substring( line.substring(0, line.lastIndexOf(" ")).lastIndexOf(" ")+1, line.lastIndexOf(" ") )));
        t.setName(line.substring(0, line.lastIndexOf(" ")).substring(0, line.substring(0, line.lastIndexOf(" ")).lastIndexOf(" ")));
        t.setName(t.getName().trim());
        String s = line.substring(line.lastIndexOf(" ")+1);
        t.setSum(s.substring(0, s.length()-3).replace(".", "") + s.substring(s.length()-3));
        return t;
    }

}
