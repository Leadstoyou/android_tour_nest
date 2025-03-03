package com.example.tour_nest.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.home.BannerAdapter;
import com.example.tour_nest.adapter.home.CategoryAdapter;
import com.example.tour_nest.adapter.home.DestinationAdapter;
import com.example.tour_nest.adapter.home.TourAdapter;
import com.example.tour_nest.databinding.ActivityHomeBinding;
import com.example.tour_nest.model.Banner;
import com.example.tour_nest.model.Category;
import com.example.tour_nest.model.Destination;
import com.example.tour_nest.model.Tour;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private DestinationAdapter destinationAdapter;
    private TourAdapter tourAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBanner();
        setupCategories();
        setupDestinations();
        setupTours();
    }

    private void setupBanner() {
        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner(R.drawable.mocchau));
        banners.add(new Banner(R.drawable.mocchau));

        bannerAdapter = new BannerAdapter(banners);
        binding.viewPagerBanner.setAdapter(bannerAdapter);
    }

    private void setupCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Du lịch sinh thái", R.drawable.mocchau));
        categories.add(new Category("Du lịch văn hóa", R.drawable.mocchau));

        categoryAdapter = new CategoryAdapter(categories);
        binding.recyclerCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerCategories.setAdapter(categoryAdapter);
    }

    private void setupDestinations() {
        List<Destination> destinations = new ArrayList<>();
        destinations.add(new Destination("Đà Nẵng", R.drawable.mocchau));
        destinations.add(new Destination("Huế", R.drawable.mocchau));

        destinationAdapter = new DestinationAdapter(destinations);
        binding.recyclerDestinations.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerDestinations.setAdapter(destinationAdapter);
    }

    private void setupTours() {
        List<Tour> tours = new ArrayList<>();
        tours.add(new Tour("Sapa", "TP. Hồ Chí Minh", "25/02/2025", "10.990.000 đ", R.drawable.mocchau));
        tours.add(new Tour("Mộc Châu", "TP. Hồ Chí Minh", "25/02/2025", "10.990.000 đ", R.drawable.mocchau));

        tourAdapter = new TourAdapter(tours);
        binding.recyclerTours.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerTours.setAdapter(tourAdapter);
    }
}
