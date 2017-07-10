package nz.net.fittings.fittingsapp.activities;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import nz.net.fittings.fittingsapp.adapters.GalleryImageDataAdapter;
import nz.net.fittings.fittingsapp.fragments.ImageFullDialogFragment;
import nz.net.fittings.fittingsapp.models.GalleryImage;



/**
 * Loads images linked to a gallery and displays them in a grid view.
 */
public class GalleryImagesActivity extends AppCompatActivity {
    //Views
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    //Adapters
    private GalleryImageDataAdapter mGalleryImageAdapter;

    //Request Queue
    private RequestQueue mRestQueue;

    //Handlers
    private GalleryRefreshSwipeListener mGalleryRefreshSwipeListener;
    private GalleryImageDataAdapter.GalleryImageClickHandler mGalleryImageClickHandler;

    //Data
    private Integer mGalleryId;
    private String mGalleryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init Views
        setContentView(R.layout.activity_gallery_images);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout_gallery_images);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_gallery_images);

        //Init Adapters
        mGalleryImageAdapter = new GalleryImageDataAdapter();
        mRecyclerView.setAdapter(mGalleryImageAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        //Init Request Queue
        mRestQueue = Volley.newRequestQueue(this);

        //Init Handlers
        mGalleryRefreshSwipeListener = new GalleryRefreshSwipeListener();
        mGalleryImageClickHandler = new GalleryImageClickHandler();

        //Set Bindings
        mSwipeRefreshLayout.setOnRefreshListener(mGalleryRefreshSwipeListener);
        mGalleryImageAdapter.setGalleryImageClickHandler(mGalleryImageClickHandler);

        //Handle Intent
        Intent imagesIntent = getIntent();
        mGalleryId = imagesIntent.getIntExtra(getString(R.string.gallery_intent_key), -1);
        mGalleryName = imagesIntent.getStringExtra(getString(R.string.gallery_intent_name));

        setTitle(mGalleryName);

        loadGalleryImages();
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
                loadGalleryImages();
                break;
            default:
                Log.i(getClass().getSimpleName(), "Behaviour has not been defined for option: " + item.getItemId());
        }
        return true;
    }


    private void loadGalleryImages() {
        mSwipeRefreshLayout.setRefreshing(true);
        String allGalleryImagesURL = getString(R.string.fittings_url) + getString(R.string.gallery_path) + "/" + mGalleryId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, allGalleryImagesURL, null, new GetGalleryImagesListener(), new GetGalleryImagesErrorListener());
        mRestQueue.add(jsonObjectRequest);
    }


    /**
     * Handles loaded galleries in a non-ui thread.
     */
    private class GetGalleryImagesListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            mSwipeRefreshLayout.setRefreshing(false);
            try {
                JSONArray jsonGalleries = response.getJSONArray("galleries");

                List<GalleryImage> galleryImages = new ArrayList<>();
                for (int i = 0; i < jsonGalleries.length(); i++) {
                    JSONObject galleryImage = jsonGalleries.getJSONObject(i);

                    String imagePath = galleryImage.getString("url");
                    URL previewUrl = new URL(getString(R.string.fittings_url) + imagePath);
                    galleryImages.add(new GalleryImage(galleryImage.getInt("id"), galleryImage.getString("name"), previewUrl));
                }

                mGalleryImageAdapter.setGalleryImages(galleryImages);
                mGalleryImageAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e(GalleryActivity.class.getSimpleName(), e.getMessage());
                showFailedToLoadToast();
            }
        }
    }



    private class GetGalleryImagesErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(GalleryActivity.class.getSimpleName(), "" + error.getMessage());
            mSwipeRefreshLayout.setRefreshing(false);
            showFailedToLoadToast();
        }
    }

    private void showFailedToLoadToast() {
        String errorMessage = MessageFormat.format(getString(R.string.gallery_failed_to_load), getString(R.string.fittings_url));
        Toast.makeText(GalleryImagesActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }


    /**
     * Re-loads the gallery images if the user drags down while at the absolute top.
     */
    private class GalleryRefreshSwipeListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            loadGalleryImages();
        }
    }



    private class GalleryImageClickHandler implements GalleryImageDataAdapter.GalleryImageClickHandler {
        @Override
        public void onClick(GalleryImage image) {
            showImageDialog(image);
        }

    }


    private void showImageDialog(GalleryImage image) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        ImageFullDialogFragment dialogFragment = ImageFullDialogFragment.newInstance(this, image);
        dialogFragment.show(ft, "dialog");
    }



}
