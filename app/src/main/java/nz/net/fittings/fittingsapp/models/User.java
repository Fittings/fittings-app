package nz.net.fittings.fittingsapp.models;


/**
 * Details about the current signed in user.
 */
public class User {
    private String username;
    private boolean canUpload;



    public User(String username, boolean canUpload) {
        this.username = username;
        this.canUpload = canUpload;
    }



    public String getUsername() {
        return this.username;
    }


    public boolean canUpload() {
        return this.canUpload;
    }


}
