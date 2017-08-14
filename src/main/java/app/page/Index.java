package app.page;

import app.util.Path;
import spark.*;

public class Index {
    public static Route servePage = (Request request, Response response) -> {
        response.redirect(Path.Web.LOGIN);
        return null;
    };
}