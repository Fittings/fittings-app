package nz.net.fittings.fittingsapp.models;


import java.net.URL;



/**
 * Metadata object containing information about an image in a gallery.
 */
public class GalleryImage {
    private int mId;
    private String mName;
    private URL mURL;



    public GalleryImage(int id, String name, URL URL) {
        this.mId = id;
        this.mName = name;
        this.mURL = URL;
    }


    public int getId() {
        return mId;
    }


    public String getName() {
        return mName;
    }


    public URL getURL() {
        return mURL;
    }


}
