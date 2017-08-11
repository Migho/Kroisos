package app.page;

import spark.*;

public class Index {
    public static Route serveIndexPage = (Request request, Response response) -> {
        return "haloo";
    };
}