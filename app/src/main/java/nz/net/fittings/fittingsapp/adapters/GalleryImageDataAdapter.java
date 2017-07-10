package nz.net.fittings.fittingsapp.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.List;
import nz.net.fittings.fittingsapp.R;
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

        Glide.with(holder.mGalleryImageView.getContext())
                .load(galleryImage.getURL())
                .into(holder.mGalleryImageView);
    }

    public void setGalleryImages(List<GalleryImage> galleryImages) {
        mGalleryImages = galleryImages;
    }


    @Override
    public int getItemCount() {
        return mGalleryImages != null ? mGalleryImages.size() : 0;
    }


    public void setGalleryImageClickHandler(GalleryImageClickHandler clickHandler) {
        mGalleryImageClickHandler = clickHandler;
    }


    class GalleryImageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mGalleryImageView;

        GalleryImageAdapterViewHolder(View view) {
            super(view);

            mGalleryImageView = view.findViewById(R.id.iv_gallery_image);
            view.findViewById(R.id.cv_gallery_image).setOnClickListener(this);
        }

        public void onClick(View v) {
            mGalleryImageClickHandler.onClick(mGalleryImages.get(getAdapterPosition()));
        }
    }

    public interface GalleryImageClickHandler {
        void onClick(GalleryImage image);
    }
}
