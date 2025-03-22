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
import com.example.tour_nest.model.Favourite;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.model.User;
import com.example.tour_nest.model.tour.Photo;
import com.example.tour_nest.service.FavouriteService;
import com.example.tour_nest.service.TourService;
import com.example.tour_nest.util.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

public class TourDetailActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private PhotoGalleryAdapter adapter;
    private List<Photo> photoList;
    private ActivityTourDetailBinding binding;
    private String tourId;
    private Tour tour = new Tour();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTourDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User user = SharedPrefHelper.getUser(this);
        userId = (user != null) ? user.getId() : null;

        tourId = getIntent().getStringExtra("tour_id");
        if (tourId == null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        loadData();
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnBookNow.setOnClickListener(v -> handleBookNow());
    }

    private void handleBookNow() {
        Intent intent = new Intent(TourDetailActivity.this, BookingActivity.class);
        tour.setId(tourId);
        intent.putExtra("tour", tour);
        startActivity(intent);
    }

    private void loadData() {
        TourService.getById(tourId).onResult(new FirebaseCallback<Tour>() {
            @Override
            public void onSuccess(Tour result) {
                binding.tourTitle.setText(result.getName());
                binding.tourCountry.setText(result.getRegion());
                binding.tourPrice.setText(String.format("$%.2f", result.getPrice()));
                binding.tourDescription.setText(result.getDescription());

                binding.tourDuration.setText(String.format("%d Days - %d Nights", result.getDays(), result.getNights()));

                binding.tourRoute.setText(String.format("%s → %s", result.getDeparture(), result.getDestination()));

                binding.tourTransportation.setText("Transportation: " + result.getTransportation());

                if (result.getPlacesToVisit() != null && !result.getPlacesToVisit().isEmpty()) {
                    binding.placesToVisit.setText(String.join(", ", result.getPlacesToVisit()));
                } else {
                    binding.placesToVisit.setText("No places specified");
                }

                if (result.getIncludedServices() != null && !result.getIncludedServices().isEmpty()) {
                    binding.includedServices.setText(String.join(", ", result.getIncludedServices()));
                } else {
                    binding.includedServices.setText("None");
                }

                if (result.getExcludedServices() != null && !result.getExcludedServices().isEmpty()) {
                    binding.excludedServices.setText(String.join(", ", result.getExcludedServices()));
                } else {
                    binding.excludedServices.setText("None");
                }

                if (result.getCancellationPolicy() != null && !result.getCancellationPolicy().isEmpty()) {
                    binding.cancellationPolicy.setText(String.join(", ", result.getCancellationPolicy()));
                } else {
                    binding.cancellationPolicy.setText("No policy specified");
                }

                if (result.getChildPolicy() != null && !result.getChildPolicy().isEmpty()) {
                    binding.childPolicy.setText(String.join(", ", result.getChildPolicy()));
                } else {
                    binding.childPolicy.setText("No policy specified");
                }

                tour = result;

                // Tải ảnh nền
                if (!isFinishing() && !isDestroyed()) {
                    Glide.with(TourDetailActivity.this)
                            .load(result.getThumbnail())
                            .override(1080, 600)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.backgroundImage);
                }

                // Cài đặt RecyclerView cho gallery
                recyclerView = findViewById(R.id.recyclerViewGallery);
                recyclerView.setLayoutManager(new GridLayoutManager(TourDetailActivity.this, 3));
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                photoList = new ArrayList<>();
                if (result.getGallery() != null) {
                    result.getGallery().forEach(img -> photoList.add(new Photo(img)));
                }
                adapter = new PhotoGalleryAdapter(TourDetailActivity.this, photoList);
                recyclerView.setAdapter(adapter);
                setRecyclerViewHeight();

                // Cập nhật trạng thái bookmark ban đầu
                if (userId != null) {
                    updateBookmarkState();
                }

                // Xử lý sự kiện nhấn nút bookmark
                binding.btnBookmark.setOnClickListener(v -> {
                    if (userId != null) {
                        toggleBookmark();
                    } else {
                        Toast.makeText(TourDetailActivity.this, "Vui lòng đăng nhập để thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(TourDetailActivity.this, "Lỗi gọi Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBookmarkState() {
        FavouriteService.getFavouriteByIdRealtime(userId, new FirebaseCallback<Favourite>() {
            @Override
            public void onSuccess(Favourite result) {
                if (!isFinishing() && !isDestroyed()) {
                    if (result != null && result.getFavouriteTours() != null && result.getFavouriteTours().contains(tourId)) {
                        binding.btnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
                    } else {
                        binding.btnBookmark.setImageResource(R.drawable.ic_bookmark);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (!isFinishing() && !isDestroyed()) {
                    binding.btnBookmark.setImageResource(R.drawable.ic_bookmark);
                }
            }
        });
    }

    private void toggleBookmark() {
        FavouriteService<Boolean> service = FavouriteService.create(tourId, userId);
        service.onResult(new FirebaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean wasAdded) {
                if (!isFinishing() && !isDestroyed()) {
                    binding.btnBookmark.setImageResource(
                            wasAdded ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark
                    );
                    Toast.makeText(TourDetailActivity.this,
                            wasAdded ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (!isFinishing() && !isDestroyed()) {
                    Toast.makeText(TourDetailActivity.this, "Lỗi khi cập nhật bookmark", Toast.LENGTH_SHORT).show();
                }
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