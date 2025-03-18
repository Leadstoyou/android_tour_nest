package com.example.tour_nest.adapter.admin;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tour_nest.R;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<Uri> imageUris; // Danh sách các URI của ảnh
    private OnRemoveImageListener removeImageListener; // Listener để xóa ảnh

    // Constructor
    public GalleryAdapter(List<Uri> imageUris, OnRemoveImageListener removeImageListener) {
        this.imageUris = imageUris;
        this.removeImageListener = removeImageListener;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_image, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);

        // Hiển thị ảnh bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(imageUri)
                .centerCrop()
                .into(holder.galleryImageView);

        // Xử lý sự kiện khi nhấn nút Xóa
        holder.removeImageButton.setOnClickListener(v -> {
            if (removeImageListener != null) {
                removeImageListener.onRemoveImage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    // ViewHolder
    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView galleryImageView;
        Button removeImageButton;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            galleryImageView = itemView.findViewById(R.id.galleryImageView);
            removeImageButton = itemView.findViewById(R.id.removeImageButton);
        }
    }

    // Interface để xóa ảnh
    public interface OnRemoveImageListener {
        void onRemoveImage(int position);
    }
}