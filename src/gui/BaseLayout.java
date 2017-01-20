package gui;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import gui.Views.DebtView;
import gui.Views.SendMailsView;

public class BaseLayout extends HorizontalSplitPanel {

    //initialize root & other components
    public BaseLayout() {
        initRoot();
        initComponents();
    }

    private void initRoot(){
        setStyleName("base-layout");
        setSplitPosition(150, Sizeable.Unit.PIXELS, false);
    }

    private void initComponents(){
        setFirstComponent(sideBar());
        //setSecondComponent(new DebtView());
        setSecondComponent(new SendMailsView());
    }

    private VerticalLayout sideBar() {
        VerticalLayout vLayout = new VerticalLayout();
        Button debtsButton = new Button("Debts", event -> changeView(1));
        Button sendMailsButton = new Button("Send mails", event -> changeView(2));

        vLayout.addComponents(debtsButton, sendMailsButton);
        return vLayout;
    }

    private void changeView(int i) {
        switch(i) {
            case 1: setSecondComponent(new DebtView()); break;
            case 2: setSecondComponent(new SendMailsView()); break;
        }
    }

}