import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import gui.LoginView;
import gui.MainView;
import services.DebtService;
import tools.TransactionFilter;
import com.vaadin.annotations.Theme;

//@Theme("Kroisos")
public class Main extends UI {

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Kroisos");
        new Navigator(this, this);
        getNavigator().addView("", LoginView.class);
        getNavigator().addView(LoginView.NAME, LoginView.class);
        getNavigator().addView(MainView.NAME, MainView.class);

        getNavigator().addViewChangeListener(new ViewChangeListener() {

            //before navigation check if user is logged in, and direct to right view.
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {

                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginView;

                if (!isLoggedIn && !isLoginView) {
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;
                } else if (isLoggedIn && isLoginView) {
                    return false;
                }
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) { }
        });
    }
}

