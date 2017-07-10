package nz.net.fittings.fittingsapp.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * Metadata object containing information about an image in a gallery.
 */
public class GalleryImage implements Parcelable {
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


    protected GalleryImage(Parcel in) {
        String[] values = new String[2];
        in.readStringArray(values);

        mId = in.readInt();
        mName = values[0];
        try {
            mURL = new URL(values[1]);
        } catch (MalformedURLException mue) {
            Log.e(this.getClass().getSimpleName(), mue.toString());
        }
    }

    public static final Creator<GalleryImage> CREATOR = new Creator<GalleryImage>() {
        @Override
        public GalleryImage createFromParcel(Parcel in) {
            return new GalleryImage(in);
        }

        @Override
        public GalleryImage[] newArray(int size) {
            return new GalleryImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);

        String[] values = new String[2];
        values[0] = mName;
        values[1] = mURL.toString();
        parcel.writeArray(values);
    }
}
