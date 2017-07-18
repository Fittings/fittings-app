package nz.net.fittings.fittingsapp.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import nz.net.fittings.fittingsapp.R;



/**
 * Handles creating and submitting a new GalleryHeader to the server.
 * GalleryHeader's consist of Name and Descriptions.
 */
public class AddGalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_gallery);

        setTitle(getString(R.string.create_gallery));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
