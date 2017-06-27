package nz.net.fittings.fittingsapp.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;
import nz.net.fittings.fittingsapp.R;
import nz.net.fittings.fittingsapp.image.FetchImageTask;
import nz.net.fittings.fittingsapp.models.GalleryImage;



/**
 * Handles displaying of loaded gallery images in a gallery_item view.
 */
public class GalleryImageDataAdapter extends RecyclerView.Adapter<GalleryImageDataAdapter.GalleryImageAdapterViewHolder> {
    private List<GalleryImage> mGalleryImages;
    private GalleryImageClickHandler mGalleryImageClickHandler;


    @Override
    public GalleryImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int listItemLayoutId = R.layout.gallery_image_item;

        View view = LayoutInflater.from(context).inflate(listItemLayoutId, parent, false);
        return new GalleryImageAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final GalleryImageAdapterViewHolder holder, int position) {
        GalleryImage galleryImage = mGalleryImages.get(position);

        //Loads the image from the URL and creates a Drawable to display the image inside of.
        if (galleryImage.getURL() != null) {
            new FetchImageTask() {
                @Override
                protected void onPostExecute(Drawable drawable) {
                    holder.mGalleryImageView.setImageDrawable(drawable);
                }
            }.execute(galleryImage.getURL());
        }
    }

    public void setGalleryImages(List<GalleryImage> galleryImages) {
        mGalleryImages = galleryImages;
    }


    @Override
    public int getItemCount() {
        return mGalleryImages != null ? mGalleryImages.size() : 0;
    }


    public void setGalleryImageClickHandler(Drawable image) {
        Log.i(this.getClass().getSimpleName(), "GalleryImageClickHandler not implemented."); //ZZZ TODO What are we doing when we click on the image?
    }


    class GalleryImageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mGalleryImageView;

        GalleryImageAdapterViewHolder(View view) {
            super(view);

            mGalleryImageView = view.findViewById(R.id.iv_gallery_image);
            view.findViewById(R.id.cv_gallery_image).setOnClickListener(this);
        }

        public void onClick(View v) {
            GalleryImage galleryImage = mGalleryImages.get(getAdapterPosition());
            Log.i(this.getClass().getSimpleName(), "on click.");
//            mClickHandler.onClick(gallery);
        }
    }



    //ZZZ TODO Something like this.
    interface GalleryImageClickHandler {
        void onClick(Drawable image);
    }
}
