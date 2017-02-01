package gui.pages;

import com.vaadin.ui.*;
import mail.MailSender;

public class SendMailsPage extends VerticalLayout {

    private MailSender mailSender = new MailSender();

    public SendMailsPage() {
        initComponents();
    }

    /**
     * First page
     */
    private TextField userName = new TextField("Helsinki.fi account username");
    private TextField senderMail = new TextField("Sender mail (only part before @helsinki.fi)");
    private TextField domain = new TextField("Host and domain", "@helsinki.fi");
    private PasswordField passwordField = new PasswordField("Password");
    private Button connect = new Button("Connect", this::connect);
    private void initComponents(){
        Button save = new Button("Connect", this::connect);
        addComponents(userName, senderMail, domain, passwordField, connect);
    }

    /**
     * Second page
     */
    private TextField eventId = new TextField("Event ID");
    private TextField status = new TextField("Status of the debts");
    private TextField subject = new TextField("Subject");
    private TextArea message = new TextArea("Message");
    private Button send = new Button("Start sending", this::send);
    private void connect(Button.ClickEvent event) {
        if(mailSender.initializeConnection(userName.getValue(), senderMail.getValue(), domain.getValue(), passwordField.getValue()) != 0) {
            //yhdistäminen ei onnistunut
            return;
        } else {
            removeAllComponents();
            addComponents(eventId, status, subject, message, send);
        }
    }

    /**
     * Third page
     */
    private void send(Button.ClickEvent event) {
        removeAllComponents();
    }

}
