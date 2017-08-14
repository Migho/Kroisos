package app.util;

import spark.Route;

public class Restrictor {

    public static Route restrict(Route path, int level) {
        //TODO check user rights. For now this wont do anything.
        return path;
    }

}
