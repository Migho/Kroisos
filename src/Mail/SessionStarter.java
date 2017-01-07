package Mail;

import java.io.Console;
import java.util.Scanner;

class SessionStarter {
    private Sender sender = new Sender();
    private Scanner s = new Scanner(System.in);
    private int previewToggle;

    /**
     * @param previewToggle True = make sure user really wants to send mail and show preview.
     */
    public SessionStarter(int previewToggle) {
        this.previewToggle = previewToggle;
    }
    public SessionStarter() { this(0); }

    private static Console consoleLoader() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            return null;
        }
        return console;
    }

    /**
     * This method will deliver the message to the Mail.Sender and will
     * ask for additional information if its necessary.
     *
     * @param toAddress Address where the message should be sent.
     * @param subject Subject of the message.
     * @param message Message of the message, duh.
     * @return 0 = success, -1 = null session, -2 = AddressException, -3 = MessagingException,
     * -10 = null console, -11 = double null session, -12 = user aborted sending.
     */
    public int sendMessage(String toAddress, String subject, String message) {
        if(previewToggle!=0) if(!preview(toAddress, subject, message)) return -12;
        if(previewToggle==1) previewToggle = 0;
        int a = sender.sendEmail(toAddress, subject, message);
        if(a==-1) {
            Console console = consoleLoader();
            if(console == null) return -10;
            String userName = console.readLine("Enter your username: ");
            String name = console.readLine("Enter your first part of the @helsinki.fi -address: ");
            String pass = String.valueOf(console.readPassword("Enter password for mail " + userName + ": "));
            sender.initializeConnection(userName, name, pass);
            a = sender.sendEmail(toAddress, subject, message);
            if(a==-1) {
                System.out.println("Couldn't create session");
                return -11;
            }
        }
        return a;
    }

    private boolean preview(String toAddress, String subject, String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("    RECEIVER: " + toAddress);
        System.out.println("    SUBJECT:  " + subject + "\n");
        System.out.println("    MESSAGE:\n" + message + "#END OF MESSAGE\n\n");
        System.out.println("Are you sure you want to send this email to " + toAddress + "? y/n");
        while(true) {
            String s = scanner.nextLine();
            if(s.equals("y")) return true;
            else if(s.equals("n")) return false;
            System.out.println("Please write \"y\" or \"n\". ");
        }

    }

}
