package Mail;

import scala.reflect.internal.pickling.UnPickler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextFileReader {

    private String text;
    private String title = "";

    /**
     * With this class you can create multiple different messages using the same template.
     * Remember: first line = title of the mail.
     * @param filePath Path to the file.
     * @return 0 for successful, -1 for file reading error.
     */
    public int readFile(String filePath) {
        File file = new File(filePath);
        try {
            Scanner scanner = new Scanner(file);
            text = new String();
            if(scanner.hasNextLine()) title = scanner.nextLine();   //First line = title of the mail
            while (scanner.hasNextLine()) text = text + scanner.nextLine() + "\r\n";
            scanner.close();
        } catch(java.io.FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            return -1;
        }
        return 0;
    }

    /**
     * This method will replace all HashMap keys matches from the text with HashMap values.
     * @param list If a key is found from the text, it will be replaces with its value.
     * @return Returns modified text.
     */
    public String textAdapter(HashMap<String, String> list) {
        String modifiedText = text;
        for(Map.Entry<String, String> entry : list.entrySet()) {
            modifiedText = modifiedText.replaceAll(entry.getKey(), entry.getValue());
        }
        return modifiedText;
    }

    /**
     * This method will replace all HashMap keys matches from the title with HashMap values.
     * @param list If a key is found from the title, it will be replaces with its value.
     * @return Returns modified title.
     */
    public String getTitle(HashMap<String, String> list) {
        String modifiedTitle = title;
        for(Map.Entry<String, String> entry : list.entrySet()) {
            modifiedTitle = modifiedTitle.replaceAll(entry.getKey(), entry.getValue());
        }
        return modifiedTitle;
    }
}
