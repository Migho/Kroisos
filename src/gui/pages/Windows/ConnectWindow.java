package gui.pages.Windows;

import com.vaadin.ui.*;
import mail.Sender;

public class ConnectWindow extends Window {

    private TextField host = new TextField("Target host", "smtp.helsinki.fi");
    private TextField userName = new TextField("Helsinki.fi account username");
    private TextField senderMail = new TextField("Sender mail (only part before @helsinki.fi)");
    private TextField domain = new TextField("Domain", "@helsinki.fi");
    private TextField port = new TextField("Port number", "587");
    private PasswordField passwordField = new PasswordField("Password");
    private TextField sleepTime = new TextField("Time to sleep between messages in ms", "10000");
    private Button save = new Button("Save");
    private Sender sender;

    public ConnectWindow() {
        super("Connect to the mail server");
        center();
        setModal(true);

        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                sender = new Sender(host.getValue(), port.getValue(), userName.getValue(), senderMail.getValue(),
                                    domain.getValue(), passwordField.getValue(), Integer.parseInt(sleepTime.getValue()));
                close();
            }
        });

        VerticalLayout content = new VerticalLayout();
        content.addComponents(host, userName, senderMail, domain, passwordField, port, sleepTime, save);
        content.setMargin(true);
        setContent(content);
    }

    public Sender getSender() {
        return sender;
    }

}
