package com.example.tour_nest.adapter.tour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.tour_nest.R;
import com.example.tour_nest.model.tour.Photo;

import java.util.List;

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.PhotoViewHolder> {
    private Context context;
    private List<Photo> photoList;

    public PhotoGalleryAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewGallery);
        }
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_gallery, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .transform(new RoundedCorners(20)) // 20dp rounded corners
                .override(300, 300)  // Resize images to 300x300 pixels
                .diskCacheStrategy(DiskCacheStrategy.ALL); // Enable caching

        Glide.with(context)
                .load(photoList.get(position).getImageUrl())
                .apply(requestOptions)
                .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                .error(R.drawable.error_image) // Fallback image in case of error
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
