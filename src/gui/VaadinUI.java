package gui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

@Theme("chameleon")
public class VaadinUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        new Navigator(this, this);

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
