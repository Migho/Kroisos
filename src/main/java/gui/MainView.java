package gui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import gui.pages.DebtsPage;
import gui.pages.MailsPage;

import java.util.LinkedList;
import java.util.List;

public class MainView extends HorizontalSplitPanel implements View {
    public static final String NAME = "main";

    private int[] asd = {1};
    private Label helloText = new Label();
    private String username = "";
    private int role = 0;

    public MainView() {
        //sidebar size
        setSplitPosition(200, Sizeable.Unit.PIXELS, false);
        setLocked(true);

        VerticalLayout sideBar = new VerticalLayout();

        helloText.setValue("Hello " + username + "!");
        Button logoutButton = new Button("Logout", (Button.ClickListener) event -> {
            getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(NAME);
        });
        sideBar.addComponents(helloText, logoutButton);

        if (1 == 1) {   //role == 1
            Label adminToolsText = new Label("Admin tools:");
            Button debtsButton = new Button("Debts", new ButtonListener("debts"));
            Button sendMailsButton = new Button("Send mails", new ButtonListener("mails"));
            sideBar.addComponents(adminToolsText, debtsButton, sendMailsButton);
        }

        for (Component c : sideBar) {
            c.setWidth("100%");
        }
        sideBar.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setFirstComponent(sideBar);
    }

    class ButtonListener implements Button.ClickListener {
        String menuitem;
        public ButtonListener(String menuitem) {
            this.menuitem = menuitem;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            getUI().getNavigator().navigateTo(NAME + "/" + menuitem);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        username = String.valueOf(getSession().getAttribute("user"));
        role = Integer.parseInt(String.valueOf(getSession().getAttribute("role")));

        if (viewChangeEvent.getParameters() == null || viewChangeEvent.getParameters().isEmpty())
            ;       //Tässä voitaisiin siirtyä sinne profiiliin joskus
        else if(viewChangeEvent.getParameters().equals(DebtsPage.NAME)) setSecondComponent(new DebtsPage());
        else if(viewChangeEvent.getParameters().equals(MailsPage.NAME)) setSecondComponent(new MailsPage());
    }
}