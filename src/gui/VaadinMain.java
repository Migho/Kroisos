package gui;

import services.DebtService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import models.Debt;

public class VaadinMain extends UI {
    @Override
    public void init(VaadinRequest request) {
        setContent(new BaseLayout());
    }
}
