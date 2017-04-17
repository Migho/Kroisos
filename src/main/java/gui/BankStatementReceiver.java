package gui;

import com.vaadin.ui.Upload;

import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.nio.file.StandardCopyOption.*;

/**
 * Created by migho on 22.3.2017.
 */

/*
public class BankStatementReceiver implements Upload.Receiver {


    private String name;
    private String from;
    private final String toFolder = "/bankStatements";

    public BankStatementReceiver(Date d1, Date d2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        name = Integer.toString(calendar.get(Calendar.DAY_OF_YEAR));
        calendar.setTime(d2);
        name += ":" + Integer.toString(calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println("Name will be " + name);
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        //Files.move(filename, "/bankStatements", ATOMIC_MOVE);
        //Files.
        return null;
    }
}
*/