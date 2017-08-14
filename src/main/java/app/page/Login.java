package app.page;

import services.LoginService;

import app.util.*;
import spark.*;
import java.util.*;

public class Login {

    public static Route servePage = (Request request, Response response) -> {
        return View.render(request, new HashMap<>(), Path.Template.LOGIN);
    };

    public static Route login = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        String userName = request.queryParams("username");
        String password = request.queryParams("password");

        if (!LoginService.login(userName, password)) { // Failure
            model.put("authenticationFailed", true);
            return View.render(request, model, Path.Template.LOGIN);
        }
        // Success
        request.session().attribute("currentUser", userName);
        request.session().attribute("userRights", 0); //TODO rights value fetch.
        response.redirect(Path.Web.PROFILE);
        return null;
    };

    public static Route logOut = (Request request, Response response) -> {
        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        response.redirect(Path.Web.LOGIN);
        return null;
    };

}
