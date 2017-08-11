package app;

import app.page.*;
import database.DebtDAO;

import spark.Filter;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class Application {

    public static DebtDAO debtDAO;

    public static void main(String[] args) {

        debtDAO = new DebtDAO();

        // Spark conf
        port(5000);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        // If user manually edit the url and forgot to add /, redirect to right url.
        before("*", (request, response) -> {
            if (!request.pathInfo().endsWith("/")) {
                response.redirect(request.pathInfo() + "/");
            }
        });

        // Routes setup
        get("/index/", Index.serveIndexPage);
    }

}
