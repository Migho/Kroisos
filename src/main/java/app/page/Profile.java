package app.page;

import app.util.Path;
import app.util.View;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class Profile {

    public static Route servePage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return View.render(request, model, Path.Template.PROFILE);
    };
}
