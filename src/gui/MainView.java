package gui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import gui.pages.DebtsPage;
import gui.pages.SendMailsPage;

public class MainView extends HorizontalSplitPanel implements View {

    static final String NAME = "";

    private Label helloText = new Label();
    private String username = "";
    private int role = 0;

    public MainView() {
        setSplitPosition(200, Sizeable.Unit.PIXELS, false);
        setLocked(true);
    }

    private void initComponents(){
        setFirstComponent(sideBar());
        //setSecondComponent();
    }

    private VerticalLayout sideBar() {
        VerticalLayout vLayout = new VerticalLayout();

        helloText.setValue("Hello " + username + "!");
        Button logoutButton = new Button("Logout", (Button.ClickListener) event -> {
            getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(NAME);
        });
        vLayout.addComponents(helloText, logoutButton);

        if(role == 1) {
            Label adminToolsText = new Label("Admin tools:");
            Button debtsButton = new Button("Debts", event -> changeView(1));
            Button sendMailsButton = new Button("Send mails", event -> changeView(2));
            vLayout.addComponents(adminToolsText, debtsButton, sendMailsButton);
        }

        for(Component c : vLayout) {
            c.setWidth("100%");
        }
        vLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        return vLayout;
    }

    private void changeView(int i) {
        switch(i) {
            case 1: setSecondComponent(new DebtsPage()); break;
            case 2: setSecondComponent(new SendMailsPage()); break;
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        username = String.valueOf(getSession().getAttribute("user"));
        role = Integer.parseInt(String.valueOf(getSession().getAttribute("role")));
        initComponents();
    }
}