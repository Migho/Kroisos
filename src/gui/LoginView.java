package gui;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import services.LoginService;

public class LoginView extends CustomComponent implements View, Button.ClickListener {

    static final String NAME = "login";
    private final TextField user;
    private final PasswordField password;
    private final Button loginButton;

    public LoginView() {
        setSizeFull();

        //User
        user = new TextField("User:");
        user.setWidth("300px");
        user.setRequired(true);
        user.setRequiredError("Please enter username");
        user.setInputPrompt("TKO-äly members username");
        user.setInvalidAllowed(false);

        //Password
        password = new PasswordField("Password:");
        password.setWidth("300px");
        password.setRequired(true);
        password.setRequiredError("Please enter password");
        password.setValue("");
        password.setNullRepresentation("");

        //Login button
        loginButton = new Button("Login", this);

        //Add to panel
        VerticalLayout fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption("KROISOS LOGIN");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //focus the username field when user arrives to the login view
        user.focus();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {

        if (!user.isValid() || !password.isValid()) return;

        String username = user.getValue();
        String password = this.password.getValue();

        if (LoginService.login(username, password)) {

            //Store the current user in the service session
            getSession().setAttribute("user", username);
            getSession().setAttribute("role", LoginService.getRole(username));
            getUI().getNavigator().navigateTo(MainView.NAME);//

        } else {this.password.setValue(null);
            this.password.focus();

        }
    }
}