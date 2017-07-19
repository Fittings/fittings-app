package nz.net.fittings.fittingsapp.activities;


import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import nz.net.fittings.fittingsapp.R;



/**
 * Handles creating and submitting a new GalleryHeader to the server.
 * GalleryHeader's consist of Name and Descriptions.
 */
public class AddGalleryActivity extends AppCompatActivity {
    private RequestQueue mRestQueue;

    private EditText mTitleEditText;
    private EditText mDescriptionEditText;

    private GallerySubmitResponseListener mGallerySubmitListener;
    private GallerySubmitFailureListener mGallerySubmitFailureListener;

    //view state
    private boolean mCanSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_gallery);

        //Init Views
        mTitleEditText = (EditText) findViewById(R.id.add_gallery_title_et);
        mDescriptionEditText = (EditText) findViewById(R.id.add_gallery_description_et);

        //Init Request Queue
        mRestQueue = Volley.newRequestQueue(this);

        //Init Handlers
        mGallerySubmitListener = new GallerySubmitResponseListener();
        mGallerySubmitFailureListener = new GallerySubmitFailureListener();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mCanSubmit = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upload, menu);

        menu.findItem(R.id.action_upload).setEnabled(mCanSubmit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //Note: We could also do this by overriding onSupportNavigateUp(...)
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_upload:
                submitGalleryHeader();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSubmitButtonEnabled(boolean enabled) {
        mCanSubmit = enabled;
        invalidateOptionsMenu();
    }


    private void submitGalleryHeader() {
        setSubmitButtonEnabled(false);

        mRestQueue.add(createGalleryHeaderRequest());
    }

    private JsonObjectRequest createGalleryHeaderRequest()
    {
        String uploadUrl = getString(R.string.fittings_url) + "/upload/gallery";

        String galleryTitle = mTitleEditText.getText().toString();
        String galleryDesc = mDescriptionEditText.getText().toString();

        JSONObject galleryHeaderJSON = new JSONObject();

        try {
            galleryHeaderJSON.put("name", galleryTitle);
            galleryHeaderJSON.put("description", galleryDesc);
        } catch (JSONException e) {
            Toast.makeText(AddGalleryActivity.this, "Failed to create Gallery Header as JSON", Toast.LENGTH_LONG).show();
        }

        return new JsonObjectRequest(Request.Method.POST, uploadUrl, galleryHeaderJSON, mGallerySubmitListener, mGallerySubmitFailureListener);
    }

    private class GallerySubmitResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            setSubmitButtonEnabled(true);
            Toast.makeText(AddGalleryActivity.this, "Gallery has uploaded!", Toast.LENGTH_LONG).show(); //ZZZ TODO Exit out.
        }
    }

    private class GallerySubmitFailureListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(AddGalleryActivity.this, "GalleryUpload failed to submit.", Toast.LENGTH_LONG).show();
            Log.e(AddGalleryActivity.class.getSimpleName(), error != null ? error.toString() : "");
        }
    }
}
