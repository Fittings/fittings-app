package nz.net.fittings.fittingsapp.image;


import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;



public class FetchImageTask extends AsyncTask<URL, Void, Drawable> {

    @Override
    protected Drawable doInBackground(URL... urls) {
        try {
            InputStream previewStream = (InputStream) urls[0].getContent();
            return Drawable.createFromStream(previewStream, "srcName");
        } catch (IOException e) {
            Log.e("FITTINGSZZZ", "Bad preview url: " + urls[0]);
            return null;
        }
    }
}
