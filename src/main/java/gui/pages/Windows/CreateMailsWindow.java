package gui.pages.Windows;

import com.vaadin.ui.*;
import mail.MessageCreator;
import models.Debt;
import services.DebtService;

/**
 * Created by migho on 20.2.2017.
 */
public class CreateMailsWindow extends Window {
    private TextField eventId = new TextField("Event number");
    private TextField eventStatus = new TextField("Event status", "PENDING");
    private TextField subject = new TextField("Message subject", "Maksuohjeet: §EVENTNAME§, §NAME§");
    private TextArea text = new TextArea();
    private Label info = new Label("You can use following codes in the text:\n" +
            "§REFERENCENUMBER§, §NAME§, §SUM§, §EVENTNAME§, §EVENTDESC§, §DUEDATE§ and §INFO§.");
    private Button save = new Button("save");

    public CreateMailsWindow() {
        super("Create mails");
        center();
        subject.setSizeFull();
        text.setSizeFull();
        text.setRows(20);
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                //real action here
                //MUISTA KORJATA KÄYTTÄJÄ
                MessageCreator mCreator = new MessageCreator(subject.getValue(), text.getValue(), "kassa");
                //mCreator.setExpDays();
                for(Debt d : DebtService.getDebts(Integer.parseInt(eventId.getValue()))) {
                    if(d.getStatus().equals(eventStatus.getValue())) {
                        System.out.println(d.getUser() + " " + d.getId());
                        if (d.getUser() != null) System.out.println(d.getUser().getId() + " " + d.getUser().getMail());
                        mCreator.createMail(d);
                    }
                }
                close();
            }
        });

        VerticalLayout content = new VerticalLayout();
        content.addComponents(eventId, eventStatus, subject, info, text, save);
        content.setMargin(true);
        setContent(content);
    }
}
