package gui.pages.Windows;

import java.util.ArrayList;
import java.util.UUID;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import models.User;
import services.DebtService;
import services.UserService;

/**
 * Created by migho on 20.2.2017.
 */
public class CreateDebtsWindow extends Window {
    private TextField eventId = new TextField("Event number");
    private TextField sum = new TextField("Price of event");
    private TextField users = new TextField("Usernames");
    private TextField mails = new TextField("Users mails");
    private Button save = new Button("Create and save");

    public CreateDebtsWindow() {
        super("Add mails");
        center();

        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String[] a = users.getValue().replaceAll("\"", "").split(", ");
                String[] b = mails.getValue().split(" ");
                ArrayList<User> c = new ArrayList<>();
                if(a.length==b.length) {
                    for(int i=0; i<a.length; i++)
                        c.add(UserService.addUser(new User(-1, UUID.randomUUID().toString(), a[i], b[i])));
                    DebtService.createEventDebts(c, Integer.parseInt(eventId.getValue()), Integer.parseInt(sum.getValue()));
                    close();
                } else System.out.println("EI VITTU: " + a.length + " " + b.length);
            }
        });

        VerticalLayout content = new VerticalLayout();
        content.addComponents(eventId, sum, users, mails, save);
        content.setMargin(true);
        setContent(content);

    }
}
