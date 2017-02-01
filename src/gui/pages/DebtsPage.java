package gui.pages;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import models.Debt;
import services.DebtService;

public class DebtsPage extends VerticalLayout {

    private Grid debtList = new Grid();

    public DebtsPage() {
        initComponents();
    }


    private void initComponents(){
        addComponents(debtList);
        debtList.setWidth("800");
        debtList.setContainerDataSource(new BeanItemContainer<>(Debt.class, DebtService.getDebts()));
        debtList.setColumnOrder("user", "eventId", "sum", "dueDate", "status", "penalty", "lastMailSent", "info");
        debtList.removeColumn("checkDigit");
        debtList.removeColumn("id");
        debtList.removeColumn("participantNumber");
        debtList.removeColumn("referenceNumber");
        debtList.removeColumn("userName");
        setMargin(true);
    }

}
