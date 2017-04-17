import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import com.vaadin.server.VaadinServlet;

@WebServlet(
    asyncSupported=false,
    urlPatterns={"/*"},
    initParams={
        @WebInitParam(name="ui", value="Main")
    })
public class Servlet extends VaadinServlet { }