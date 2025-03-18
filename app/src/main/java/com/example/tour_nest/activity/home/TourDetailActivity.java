package com.example.tour_nest.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tour_nest.R;
import com.example.tour_nest.adapter.tour.PhotoGalleryAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityTourDetailBinding;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.model.tour.Photo;
import com.example.tour_nest.service.TourService;

import java.util.ArrayList;
import java.util.List;

public class TourDetailActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private PhotoGalleryAdapter adapter;
    private List<Photo> photoList;
    private ActivityTourDetailBinding binding;
    private String tourId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTourDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tourId = getIntent().getStringExtra("tour_id");
        if (tourId == null) {
            startActivity(new Intent(this, HomeActivity.class));
        }
        loadData();
    }

    private void loadData() {
        TourService.getById(tourId).onResult(new FirebaseCallback<Tour>() {
            @Override
            public void onSuccess(Tour result) {

                binding.tourTitle.setText(result.getName());
                binding.tourCountry.setText(result.getRegion());
                binding.tourPrice.setText(String.valueOf(result.getPrice()));
                binding.tourDescription.setText(result.getDescription());

                Glide.with(TourDetailActivity.this)
                        .load(result.getThumbnail())
                        .override(1080, 600)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.backgroundImage);

                recyclerView = findViewById(R.id.recyclerViewGallery);
                recyclerView.setLayoutManager(new GridLayoutManager(TourDetailActivity.this, 3));
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                photoList = new ArrayList<>();
                result.getGallery().forEach(img -> {
                    photoList.add(new Photo(img));
                });
                adapter = new PhotoGalleryAdapter(TourDetailActivity.this, photoList);
                recyclerView.setAdapter(adapter);
                setRecyclerViewHeight();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(TourDetailActivity.this, "Loi goi firebase", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setRecyclerViewHeight() {
        recyclerView.post(() -> {
            if (adapter.getItemCount() > 0) {
                int rowCount = (int) Math.ceil(adapter.getItemCount() / 3.0); // 3 images per row
                int itemHeight = 100; // Height of each imageView in dp
                int totalHeight = rowCount * itemHeight; // Calculate total height

                float density = getResources().getDisplayMetrics().density;
                int totalHeightPx = (int) (totalHeight * density);

                // Set the calculated height
                recyclerView.getLayoutParams().height = totalHeightPx;
                recyclerView.requestLayout();
            }
        });
    }

}