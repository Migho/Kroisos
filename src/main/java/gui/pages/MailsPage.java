package gui.pages;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import gui.pages.Windows.ConnectWindow;
import gui.pages.Windows.CreateMailsWindow;
import gui.pages.Windows.SendMailsWindow;
import mail.Sender;
import models.Mail;
import services.MailService;

public class MailsPage extends VerticalLayout {

    public static final String NAME = "mails";
    private Sender sender;

    public MailsPage() {
        initComponents();
    }

    private Grid mailList = new Grid();
    private Button createMails = new Button("Create new mails");
    private Button connect = new Button("Connection settings");
    private Button sendMails = new Button("Send mails");
    private HorizontalLayout actionButtons = new HorizontalLayout(createMails, connect, sendMails);
    private ConnectWindow cWindow = new ConnectWindow();
    private SendMailsWindow sWindow;

    public void initComponents(){
        createMails.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(new CreateMailsWindow());

            }
        });
        connect.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(cWindow);
            }
        });
        sendMails.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                sWindow = new SendMailsWindow(cWindow.getSender());
                UI.getCurrent().addWindow(sWindow);
            }
        });

        mailList.setContainerDataSource(new BeanItemContainer<>(Mail.class, MailService.getMails()));
        mailList.setSizeFull();
        addComponents(actionButtons, mailList);
    }



}