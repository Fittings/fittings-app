package nz.net.fittings.fittingsapp.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import nz.net.fittings.fittingsapp.R;
import nz.net.fittings.fittingsapp.models.Gallery;



/**
 * Handles displaying of loaded {@link nz.net.fittings.fittingsapp.models.Gallery} objects
 * in a gallery_item view.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryAdapterViewHolder> {
    private List<Gallery> mGalleries;
    private GalleryClickHandler mClickHandler;


    @Override
    public GalleryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int listItemLayoutId = R.layout.gallery_item;

        View view = LayoutInflater.from(context).inflate(listItemLayoutId, parent, false);
        return new GalleryAdapterViewHolder(view);
    }


    @Override
    public void onViewRecycled(GalleryAdapterViewHolder holder) {
        super.onViewRecycled(holder);
        Context context = holder.mGalleryImageView.getContext();
        Glide.with(context).clear(holder.mGalleryImageView);
    }

    @Override
    public void onBindViewHolder(final GalleryAdapterViewHolder holder, int position) {

        Gallery gallery = mGalleries.get(position);
        Context context = holder.mGalleryImageView.getContext();

        if (gallery.getPreviewImageURL() != null) {
            //Loads the image from the URL and creates a Drawable to display the image inside of.
            Glide.with(context)
                    .setDefaultRequestOptions(RequestOptions.centerCropTransform().placeholder(R.color.cardview_light_background))
//                    .setDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_favicon).fallback(R.mipmap.ic_placeholder))
                    .load(gallery.getPreviewImageURL())
                    .into(holder.mGalleryImageView);
        }


        holder.mGalleryTitleTextView.setText(gallery.getName());
        holder.mGalleryDescriptionTextView.setText(gallery.getDescription());
    }


    public void addGalleryClickHandler(GalleryClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }


    @Override
    public int getItemCount() {
        return mGalleries != null ? mGalleries.size() : 0;
    }


    public void setGalleries(List<Gallery> galleries) {
        this.mGalleries = galleries;
    }


    public interface GalleryClickHandler {
        void onClick(Gallery gallery);
    }



    /**
     * ViewHolder for caching loaded galleries.
     * Manages the display for the card fields and ClickHandler.
     */
    class GalleryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mGalleryTitleTextView;
        private final TextView mGalleryDescriptionTextView;
        private final ImageView mGalleryImageView;


        GalleryAdapterViewHolder(View view) {
            super(view);
            mGalleryTitleTextView = view.findViewById(R.id.tv_gallery_title);
            mGalleryDescriptionTextView = view.findViewById(R.id.tv_gallery_description);
            mGalleryImageView = view.findViewById(R.id.iv_gallery_preview_image);

            view.findViewById(R.id.cv_gallery).setOnClickListener(this);
        }

        public void onClick(View v) {
            Gallery gallery = mGalleries.get(getAdapterPosition());
            mClickHandler.onClick(gallery);
        }
    }
}
