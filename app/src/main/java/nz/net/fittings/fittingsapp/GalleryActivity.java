package nz.net.fittings.fittingsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class GalleryActivity extends AppCompatActivity {
    private static String fittingsBaseUrl = "http://10.0.2.2:8000";
    private static String gallerysURLPath = "/gallery";

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        queue = Volley.newRequestQueue(this);

        doGetGalleries();
    }

    private void doGetGalleries()
    {
        String allGalleriesURL = fittingsBaseUrl + gallerysURLPath + "/all";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, allGalleriesURL, null, new GetGalleriesListener(), new ErrorListener());
        queue.add(jsonObjectRequest);
    }

    private class GetGalleriesListener implements Response.Listener<JSONObject> {

        @Override
        public void onResponse(JSONObject response) {
            Log.w("ZZZ Fittings", "response: " + response.toString());
            try {
                JSONArray galleries = response.getJSONArray("galleries");
                for (int i=0; i < galleries.length(); i++) {
                    JSONObject gallery = galleries.getJSONObject(i);
                    Log.w("ZZZ Fittings", gallery.get("description") + " : " + gallery.get("name") + " : " + gallery.get("id"));
                }
            } catch (Exception e) {
                Log.w("ZZZ Fittings", "bad get");
            }
        }
    }

    private class ErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }


}
