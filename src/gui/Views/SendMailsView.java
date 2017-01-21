package gui.Views;

import com.vaadin.ui.*;
import mail.MailSender;

public class SendMailsView extends VerticalLayout {

    MailSender mailSender = new MailSender();

    public SendMailsView() {
        initRoot();
        initComponents();
    }

    private void initRoot(){
        setStyleName("vertical-menu");
    }

    /**
     * First page
     */
    TextField userName = new TextField("Helsinki.fi account username");
    TextField senderMail = new TextField("Sender mail (only part before @helsinki.fi)");
    TextField domain = new TextField("Host and domain", "@helsinki.fi");
    PasswordField passwordField = new PasswordField("Password");
    Button connect = new Button("Connect", this::connect);
    private void initComponents(){
        Button save = new Button("Connect", this::connect);
        addComponents(userName, senderMail, domain, passwordField, connect);
    }

    /**
     * Second page
     */
    TextField eventId = new TextField("Event ID");
    TextField status = new TextField("Status of the debts");
    TextField subject = new TextField("Subject");
    TextArea message = new TextArea("Message");
    Button send = new Button("Start sending", this::send);
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
    public void send(Button.ClickEvent event) {
        removeAllComponents();
    }

}
