package app;

import app.page.*;
import app.util.*;
import database.DebtDAO;

import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class Application {

    public static DebtDAO debtDAO;

    public static void main(String[] args) {

        debtDAO = new DebtDAO();

        //Spark
        port(5000);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        //If user manually edit the url and forgot to add /, redirect to right url.
        before("*", (request, response) -> {
            if (!request.pathInfo().endsWith("/")) {
                response.redirect(request.pathInfo() + "/");
            }
        });

        /*
         * Routes and permissions setup.
         * If page requires logging in, it must go trough restrictor.
         * Level 0 is available for everyone. Levels are described later.
         */
        get("/", Index.servePage);
        get(Path.Web.INDEX, Index.servePage);
        get(Path.Web.LOGIN, Login.servePage);
        get(Path.Web.PROFILE, Restrictor.restrict(Profile.servePage, 0));
        post(Path.Web.LOGIN, Login.login);
        post(Path.Web.LOGOUT, Login.logOut);
    }

}
