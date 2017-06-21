package nz.net.fittings.fittingsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nz.net.fittings.fittingsapp.adapters.GalleryAdapter;
import nz.net.fittings.fittingsapp.models.Gallery;

public class GalleryActivity extends AppCompatActivity {
    private RequestQueue restQueue;

    private RecyclerView mRecyclerView;
    private GalleryAdapter mGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        restQueue = Volley.newRequestQueue(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_galleries);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mGalleryAdapter = new GalleryAdapter(new GalleryAdapter.GalleryClickHandler() {
            @Override
            public void onClick(Gallery gallery) {
                //ZZZ TODO expand or something.
            }
        });
        mRecyclerView.setAdapter(mGalleryAdapter);

        loadGalleriesData();
    }

    private void loadGalleriesData()
    {
        //ZZZ TODO show progress bar
        String allGalleriesURL = getString(R.string.fittings_url) + getString(R.string.gallery_path) + "/all";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, allGalleriesURL, null, new GetGalleriesListener(), new ErrorListener());
        restQueue.add(jsonObjectRequest);
    }

    private class GetGalleriesListener implements Response.Listener<JSONObject> {

        @Override
        public void onResponse(JSONObject response) {
            //ZZZ TODO hide progress bar.
            try {
                JSONArray jsonGalleries = response.getJSONArray("galleries");

                List<Gallery> galleries = new ArrayList<>();
                for (int i=0; i < jsonGalleries.length(); i++) {
                    JSONObject gallery = jsonGalleries.getJSONObject(i);
                    String previewPath = gallery.getString("preview_url");
                    URL previewUrl = new URL(getString(R.string.fittings_url) + previewPath);
                    galleries.add(new Gallery(gallery.getInt("id"), gallery.getString("name"), gallery.getString("description"), previewUrl));
                }

                mGalleryAdapter.setGalleries(galleries);
                mGalleryAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                //ZZZ TODO Display toasty error.
                Log.e("FITTINGSZZZ", "This error wont occur. " + e.getMessage());
            }
        }
    }

    private class ErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("FITTINGSZZZ", "ErrorListener: " + error.getMessage());
        }
    }


}
