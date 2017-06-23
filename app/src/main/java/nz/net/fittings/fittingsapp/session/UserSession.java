package nz.net.fittings.fittingsapp.session;


import android.util.Log;
import java.util.Date;
import nz.net.fittings.fittingsapp.models.User;



/**
 * Manages the users current state
 */
public final class UserSession {
    private static UserSession session;

    private User user;
    private Date getSessionStartDate; //ZZZ TODO Update to java8...



    //Note: User session is a singleton, call UserSession.initialiseUserSession(...) instead.
    private UserSession(User user, Date date) {
        this.user = user;
        this.getSessionStartDate = date;
    }


    public static void initialiseUserSession(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null. To end a session call UserSession.endCurrentUserSession(...)");

        Log.i(UserSession.class.getSimpleName(), "New sign-in session for: " + user.getUsername());
        session = new UserSession(user, new Date());
    }

    public static void endCurrentUserSession() {
        session = null;
    }

    public static UserSession getUserSesssion() {
        return session;
    }

    public String getUsername() {
        return this.user.getUsername();
    }

    public Date getSessionStartDate() {
        return this.getSessionStartDate;
    }












}
