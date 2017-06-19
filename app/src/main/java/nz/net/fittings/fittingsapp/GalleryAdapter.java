package nz.net.fittings.fittingsapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;



public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryAdapterViewHolder> {
    private List<Gallery> mGalleries;
    private GalleryClickHandler mClickHandler;



    public GalleryAdapter(GalleryClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }



    @Override
    public GalleryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int listItemLayoutId = R.layout.gallery_item;

        View view = LayoutInflater.from(context).inflate(listItemLayoutId, parent, false);
        return new GalleryAdapterViewHolder(view);
    }



    @Override
    public void onBindViewHolder(GalleryAdapterViewHolder holder, int position) {
        Gallery gallery = mGalleries.get(position);
        holder.mGalleryTextView.setText("id: " + gallery.getId() + ", name: " + gallery.getDescription() + ", description: " + gallery.getDescription());
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

    public class GalleryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mGalleryTextView;

        public GalleryAdapterViewHolder(View view) {
            super(view);
            mGalleryTextView = (TextView) view.findViewById(R.id.tv_gallery_data);

            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            Gallery gallery = mGalleries.get(getAdapterPosition());
            mClickHandler.onClick(gallery);
        }
    }
}
