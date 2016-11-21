package Bookkeeping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by migho on 29.9.2016.
 */
public class FileAccessor {

    File file;
    private List<Debt> list = new ArrayList<>();

    public FileAccessor(String filePath) {
        this.file = new File(filePath);
        try {
            Scanner lukija = new Scanner(file);
            while (lukija.hasNextLine()) addDebt(parseLine(lukija.nextLine()));
            lukija.close();
            deleteDuplicates();
        } catch(java.io.FileNotFoundException e1) {
            System.out.println("File not found: " + filePath);
            try {
                file.createNewFile();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public List<Debt> getDebts() {
        return list;
    }
    public Debt getDebt(long referenceNumber) {
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getReferenceNumber() == referenceNumber) return list.get(i);
        }
        return null;
    }
    public int removeDebt(long referenceNumber) {
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getReferenceNumber() == referenceNumber){
                list.remove(i);
                return 0;
            }
        }
        return -1;
    }
    public int addDebt(Debt debt) {
        if(debt == null) return -2;
        if(list.contains(debt)) return -1;
        list.add(debt);
        return 0;
    }
    public int writeDebts() {
        try {
            Collections.sort(list);
            PrintWriter writer = new PrintWriter(file);
            for(int i=0; i<list.size(); i++) {
                Debt d = list.get(i);
                writer.println(d.eventNumber + "§" + d.participantNumber + "§" + d.checkDigit + "§" + d.mail +
                        "§" + d.name + "§" + d.sum + "§" + d.externalInfo);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't write file " + file + ": " + e);
            return -1;
        }
        return 0;
    }
    public List<Debt> getAllEventDebts(int eventNumber) {
        List<Debt> a = new ArrayList<Debt>();
        for(int i=0; i<list.size(); i++)
            if(list.get(i).eventNumber == eventNumber) a.add(list.get(i));
        return a;
    }
    private Debt parseLine(String line) {
        String splittedLine[] = line.split("§");
        if(splittedLine.length < 6) {
            System.out.println("INCOMPATIBLE LINE");
            return null;
        }
        Debt debt = new Debt();
        debt.eventNumber = Integer.parseInt(splittedLine[0]);
        debt.participantNumber = Integer.parseInt(splittedLine[1]);
        debt.checkDigit = Integer.parseInt(splittedLine[2]);
        debt.mail = splittedLine[3];
        debt.name = splittedLine[4];
        debt.sum = Double.parseDouble(splittedLine[5]);
        if(splittedLine.length >= 6) {
            debt.externalInfo = splittedLine[4];
        }
        return debt;
    }
    private void deleteDuplicates() {
        if(list.size()<=1) return;
        Collections.sort(list);
        Debt previous = list.get(0);
        for(int i=1; i<list.size(); i++) {
            Debt next = list.get(i);
            if(previous.toString() == next.toString()) {
                list.remove(i);
                i--;
            }
            previous = next;
        }
    }
}
