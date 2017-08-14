package app.util;

import lombok.Getter;

public class Path {

    public static class Web {
        @Getter public static final String INDEX = "/index/";
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String PROFILE = "/profile/";
    }

    public static class Template {
        public final static String LOGIN = "/velocity/login.vm";
        public final static String PROFILE = "/velocity/profile.vm";
    }

}