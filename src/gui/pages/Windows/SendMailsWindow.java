package gui.pages.Windows;

import com.vaadin.ui.*;
import mail.Sender;

/**
 * Created by migho on 20.2.2017.
 */
public class SendMailsWindow extends Window {
    private TextField eventId = new TextField("Event ID. If 0, then no event preference", "0");
    private TextField status = new TextField("Status of the debts");
    private Button send = new Button("Start sending");
    private VerticalLayout content = new VerticalLayout();

    public SendMailsWindow(Sender sender) {
        super("Send mails");
        center();

        if(sender == null) {
            content.addComponent(new Label("You need to specify connection settings first"));
        } else {
            send.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    sender.setTarget(Integer.parseInt(eventId.getValue()), status.getValue());
                    sender.start();
                    close();
                }
            });
            content.addComponents(eventId, status, send);
            content.setMargin(true);
        }
        setContent(content);
    }
}
