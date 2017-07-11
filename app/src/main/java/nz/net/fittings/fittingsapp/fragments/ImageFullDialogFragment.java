package nz.net.fittings.fittingsapp.fragments;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import nz.net.fittings.fittingsapp.R;
import nz.net.fittings.fittingsapp.models.GalleryImage;



public class ImageFullDialogFragment extends DialogFragment {
    private GalleryImage mImage;

    public static ImageFullDialogFragment newInstance(Context context, GalleryImage image) {
        ImageFullDialogFragment fragment = new ImageFullDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.image_preview), image);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImage = savedInstanceState != null ? (GalleryImage) savedInstanceState.getParcelable(getString(R.string.image_preview))
                                            : (GalleryImage) getArguments().getParcelable(this.getString(R.string.image_preview));

        this.setStyle(R.style.ThemeDialog, R.style.AppTheme);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(getString(R.string.image_preview), mImage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fullscreen_image, container, false);

        PhotoView imageView = dialogView.findViewById(R.id.fullscreen_image);
        Log.i("ZOOOOMING?", "isZoomEnabled" + imageView.isZoomEnabled());
        Log.i(this.getClass().getSimpleName(), "mImage" + mImage);
        Glide.with(this)
                .load(mImage.getURL())
                .into(imageView);


        return dialogView;
    }

}

