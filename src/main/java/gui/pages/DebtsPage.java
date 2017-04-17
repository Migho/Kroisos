
package gui.pages;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import gui.pages.Windows.CreateDebtsWindow;
import models.Debt;
import services.DebtService;

public class DebtsPage extends VerticalLayout {

    public static final String NAME = "debts";

    private Grid debtList = new Grid();
    private Button addDebts = new Button("Add debts");

    public DebtsPage() {
        initComponents();
    }


    private void initComponents(){
        debtList.setWidth("800");
        debtList.setContainerDataSource(new BeanItemContainer<>(Debt.class, DebtService.getDebts()));
        debtList.setColumnOrder("user", "referenceNumber", "eventId", "sum", "dueDate", "status", "penalty", "lastMailSent", "info");
        debtList.removeColumn("checkDigit");
        debtList.removeColumn("id");
        debtList.removeColumn("participantNumber");
        debtList.removeColumn("userName");
        setMargin(true);

        addDebts.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(new CreateDebtsWindow());
            }
        });

        debtList.setSizeFull();
        addComponents(addDebts, debtList);
    }

}