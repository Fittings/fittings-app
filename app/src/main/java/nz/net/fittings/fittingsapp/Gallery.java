package nz.net.fittings.fittingsapp;



public class Gallery {
    private int mId;
    private String mName;
    private String mDescription;

    public Gallery(int id, String name, String description) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

}
