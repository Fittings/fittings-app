package nz.net.fittings.fittingsapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import nz.net.fittings.fittingsapp.R;
import nz.net.fittings.fittingsapp.adapters.GalleryAdapter;
import nz.net.fittings.fittingsapp.models.Gallery;



/**
 * Displays gallery previews that are stored on the
 * <a href=https://fittings.net.nz>fittings.net.nz</a> server.
 */
public class GalleryActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private GalleryAdapter mGalleryAdapter;
    private RequestQueue mRestQueue;

    //Constant
    private static final int CREATE_GALLERY = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init Views
        setContentView(R.layout.activity_gallery);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout_galleries);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_galleries);

        //Init Adapters
        mGalleryAdapter = new GalleryAdapter();
        mRecyclerView.setAdapter(mGalleryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        //Init Request Queue
        mRestQueue = Volley.newRequestQueue(this);

        //Set Bindings
        findViewById(R.id.fab_add_gallery).setOnClickListener(new AddGalleryClickHandler());
        mGalleryAdapter.addGalleryClickHandler(new GalleryClickHandler());
        mSwipeRefreshLayout.setOnRefreshListener(new GalleryRefreshSwipeListener());

        setTitle(getString(R.string.galleries));
        findViewById(R.id.fab_add_gallery).setVisibility(View.VISIBLE); //ZZZ TODO Tie this into user authentication.

        loadGalleriesData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadGalleriesData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CREATE_GALLERY:
                //ZZZ TODO Do we need to actually do anything on response..?
        }
    }


    /**
     * Creates a request to <a href=https://fittings.net.nz>fittings.net.nz</a>
     * and sets the view on callback.
     */
    private void loadGalleriesData() {
        mSwipeRefreshLayout.setRefreshing(true);
        String allGalleriesURL = getString(R.string.fittings_url) + getString(R.string.gallery_path) + "/all";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, allGalleriesURL, null, new GetGalleriesListener(), new ErrorListener());
        mRestQueue.add(jsonObjectRequest);
    }


    private void showFailedToLoadToast() {
        String errorMessage = MessageFormat.format(getString(R.string.gallery_failed_to_load), getString(R.string.fittings_url));
        Toast.makeText(GalleryActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }


    /**
     * Handles loaded galleries in a non-ui thread.
     */
    private class GetGalleriesListener implements Response.Listener<JSONObject> {

        @Override
        public void onResponse(JSONObject response) {
            mSwipeRefreshLayout.setRefreshing(false);
            try {
                JSONArray jsonGalleries = response.getJSONArray("galleries");

                List<Gallery> galleries = new ArrayList<>();
                for (int i = 0; i < jsonGalleries.length(); i++) {
                    JSONObject gallery = jsonGalleries.getJSONObject(i);

                    URL previewUrl = !gallery.isNull("preview_url")
                            ? new URL(getString(R.string.fittings_url) + gallery.getString("preview_url"))
                            : null;

                    galleries.add(new Gallery(gallery.getInt("id"), gallery.getString("name"), gallery.getString("description"), previewUrl));
                }

                mGalleryAdapter.setGalleries(galleries);
                mGalleryAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e(GalleryActivity.class.getSimpleName(), e.getMessage());
                showFailedToLoadToast();
            }
        }
    }



    /**
     * Starts a new activity that displays all the images in the gallery.
     */
    private class GalleryClickHandler implements GalleryAdapter.GalleryClickHandler {
        @Override
        public void onClick(Gallery gallery) {
            Intent showGalleryImagesIntent = new Intent(GalleryActivity.this, GalleryImagesActivity.class);
            showGalleryImagesIntent.putExtra(getString(R.string.gallery_intent_key), gallery.getId());
            showGalleryImagesIntent.putExtra(getString(R.string.gallery_intent_name), gallery.getName());
            startActivity(showGalleryImagesIntent);
        }
    }



    /**
     * Re-loads the galleries if the user drags down while at the absolute top.
     */
    private class GalleryRefreshSwipeListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            loadGalleriesData();
        }
    }



    private class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(GalleryActivity.class.getSimpleName(), "" + error.getMessage());
            mSwipeRefreshLayout.setRefreshing(false);
            showFailedToLoadToast();
        }
    }

    private class AddGalleryClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent addGalleryIntent = new Intent(GalleryActivity.this, AddGalleryActivity.class);
            startActivity(addGalleryIntent);
        }
    }


}
