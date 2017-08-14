package app.util;

import org.apache.velocity.app.*;
import spark.*;
import spark.template.velocity.*;
import java.util.*;

public class View {

    /*
     * This class will print literal HTML for the spark.
     */
    public static String render(Request request, Map<String, Object> model, String templatePath) {
        model.put("msg", new Message(request.session().attribute("locale")));
        model.put("currentUser", request.session().attribute("currentUser"));
        model.put("WebPath", Path.Web.class);

        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine).render(new ModelAndView(model, templatePath));
    }
}
