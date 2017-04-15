package services;

import models.User;

/**
 * Tähän luokkaan pitäis koodailla jokin tsydeemi millä saadaan tunnareita TKO-älyn members-sivuilta.
 * Kattelis syssymmällä, testailuissa menee näillä.
 */
public class LoginService {

    public static boolean login(String userName, String password) {
        if(userName.equals("kassamies") && password.equals("parasjäbä")) return true;
        if(userName.equals("rahisjäbä") && password.equals("homo")) return true;
        return false;
    }

    public static int getRole(String username) {
        if(username.equals("kassamies")) return 1;
        else return 0;
    }

}