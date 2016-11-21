package Mail;

import scala.reflect.internal.pickling.UnPickler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextFileReader {

    private String text;
    private String title = "";

    public int readFile(String filePath) {
        File file = new File(filePath);
        try {
            Scanner lukija = new Scanner(file);
            text = new String();
            if(lukija.hasNextLine()) title = lukija.nextLine();   //First line = title of the mail
            while (lukija.hasNextLine()) text = text + lukija.nextLine() + "\r\n";
            lukija.close();
        } catch(java.io.FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            return -1;
        }
        return 0;
    }

    public String textAdapter(HashMap<String, String> list) {
        String modifiedText = text;
        for(Map.Entry<String, String> entry : list.entrySet()) {
            modifiedText = modifiedText.replaceAll(entry.getKey(), entry.getValue());
        }
        return modifiedText;
    }
    public String getTitle(HashMap<String, String> list) {
        String modifiedTitle = title;
        for(Map.Entry<String, String> entry : list.entrySet()) {
            modifiedTitle = modifiedTitle.replaceAll(entry.getKey(), entry.getValue());
        }
        return modifiedTitle;
    }
}
