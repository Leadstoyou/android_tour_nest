package com.example.tour_nest.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tour_nest.R;
import com.example.tour_nest.adapter.tour.PhotoGalleryAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.databinding.ActivityTourDetailBinding;
import com.example.tour_nest.model.tour.Photo;

import java.util.ArrayList;
import java.util.List;

public class TourDetailActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private PhotoGalleryAdapter adapter;
    private List<Photo> photoList;
    private ActivityTourDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTourDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this)
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQtGXVtvF1dpvRTfBKnJwvXnvK-nAcGOqch3w&s") // Use drawable resource OR a URL
                .override(1080, 600)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.backgroundImage);

        //-------------------------------------------------------------------
        recyclerView = findViewById(R.id.recyclerViewGallery);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false); // Prevent scroll conflict
        // Sample images from the internet (Replace with your own URLs)
        photoList = new ArrayList<>();
        photoList.add(new Photo("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTt3ypdKlgfJP8tiw1aaI3qV8QcdzV0Lk8JXQ&s"));
        photoList.add(new Photo("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTt3ypdKlgfJP8tiw1aaI3qV8QcdzV0Lk8JXQ&s"));
        photoList.add(new Photo("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTt3ypdKlgfJP8tiw1aaI3qV8QcdzV0Lk8JXQ&s"));
        photoList.add(new Photo("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTt3ypdKlgfJP8tiw1aaI3qV8QcdzV0Lk8JXQ&s"));
        photoList.add(new Photo("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTt3ypdKlgfJP8tiw1aaI3qV8QcdzV0Lk8JXQ&s"));
        adapter = new PhotoGalleryAdapter(this, photoList);
        recyclerView.setAdapter(adapter);
        setRecyclerViewHeight();

    }

    private void setRecyclerViewHeight() {
        recyclerView.post(() -> {
            if (adapter.getItemCount() > 0) {
                int rowCount = (int) Math.ceil(adapter.getItemCount() / 3.0); // 3 images per row
                int itemHeight = 100; // Height of each imageView in dp
                int totalHeight = rowCount * itemHeight; // Calculate total height

                // Convert dp to pixels
                float density = getResources().getDisplayMetrics().density;
                int totalHeightPx = (int) (totalHeight * density);

                // Set the calculated height
                recyclerView.getLayoutParams().height = totalHeightPx;
                recyclerView.requestLayout();
            }
        });
    }

}