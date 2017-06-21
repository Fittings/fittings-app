package nz.net.fittings.fittingsapp.models;


import java.net.URL;



public class Gallery {

    private int mId;
    private String mName;
    private String mDescription;
    private URL mPreviewImage;



    public Gallery(int id, String name, String description, URL previewImage) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mPreviewImage = previewImage;
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

    public URL getPreviewImageURL() { return mPreviewImage; }

}
