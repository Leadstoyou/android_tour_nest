package com.example.tour_nest.activity.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.home.CategoryAdapter;
import com.example.tour_nest.adapter.home.CategorySliderAdapter;
import com.example.tour_nest.adapter.home.PlaceAdapter;
import com.example.tour_nest.adapter.home.ServiceAdapter;
import com.example.tour_nest.adapter.home.TourAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.model.home.Category;
import com.example.tour_nest.model.home.CategorySlider;
import com.example.tour_nest.model.home.Place;
import com.example.tour_nest.model.home.Service;
import com.example.tour_nest.model.home.TourPackage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements CategoryAdapter.OnCategoryClickListener {
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    private ViewPager2 tourSlider;
    private TourAdapter tourAdapter;
    private List<TourPackage> tourList;


    private RecyclerView categorSlideryRecyclerView;
    private CategorySliderAdapter categorySliderAdapter;
    private List<CategorySlider> categorySliderList;


    private MaterialButton btnSolo, btnFamily;

    private RecyclerView placeRecyclerView;
    private PlaceAdapter placeAdapter;
    private List<Place> placeList;
    private BottomNavigationView bottomNavigationView;

    private RecyclerView servicesRecyclerView;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        categoryList = new ArrayList<>();
        categoryList.add(new Category("All", false));
        categoryList.add(new Category("Asia", true)); // Mặc định chọn
        categoryList.add(new Category("Europe", false));
        categoryList.add(new Category("America", false));
        categoryList.add(new Category("Oceania", false));

        categoryAdapter = new CategoryAdapter(categoryList, this);
        categoryRecyclerView.setAdapter(categoryAdapter);


        tourSlider = findViewById(R.id.tourSlider);
        //--------------------------------
        categorSlideryRecyclerView = findViewById(R.id.categorySliderRecyclerView);
        categorSlideryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        categorySliderList = new ArrayList<>();
        categorySliderList.add(new CategorySlider(R.drawable.ic_mountain, "Mountain"));
        categorySliderList.add(new CategorySlider(R.drawable.ic_beach, "Beach"));
        categorySliderList.add(new CategorySlider(R.drawable.ic_history, "History"));

        categorySliderAdapter = new CategorySliderAdapter(this, categorySliderList);
        categorSlideryRecyclerView.setAdapter(categorySliderAdapter);


        // Tạo danh sách Tour Packages
        tourList = new ArrayList<>();
        tourList.add(new TourPackage("https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcQSXSYMy134UZwQw2ZFN-nCLwjZInk4mr6azYQVFwwEImkHNEpzSJ1Rb8aTVdGkUI7sgQ4TdU0gX-qcGLnI1AEKvWMJWL2-T2wz5npdoQ", "Maldives Package", "South Asia", 4.6f));
        tourList.add(new TourPackage("https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcQSXSYMy134UZwQw2ZFN-nCLwjZInk4mr6azYQVFwwEImkHNEpzSJ1Rb8aTVdGkUI7sgQ4TdU0gX-qcGLnI1AEKvWMJWL2-T2wz5npdoQ", "Paris Getaway", "France", 4.8f));
        tourList.add(new TourPackage("https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcQSXSYMy134UZwQw2ZFN-nCLwjZInk4mr6azYQVFwwEImkHNEpzSJ1Rb8aTVdGkUI7sgQ4TdU0gX-qcGLnI1AEKvWMJWL2-T2wz5npdoQ", "Bali Adventure", "Indonesia", 4.7f));

        tourAdapter = new TourAdapter(this, tourList);
        tourSlider.setAdapter(tourAdapter);

        //------------------------------------
        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroup);
        btnSolo = findViewById(R.id.btnSolo);
        btnFamily = findViewById(R.id.btnFamily);

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.btnSolo) {
                btnSolo.setBackgroundTintList(getResources().getColorStateList(R.color.light_blue));
                btnSolo.setTextColor(getResources().getColor(R.color.white));

                btnFamily.setBackgroundTintList(getResources().getColorStateList(R.color.lighter_blue));
                btnFamily.setTextColor(getResources().getColor(R.color.light_blue));

            } else if (checkedId == R.id.btnFamily) {
                btnFamily.setBackgroundTintList(getResources().getColorStateList(R.color.light_blue));
                btnFamily.setTextColor(getResources().getColor(R.color.white));

                btnSolo.setBackgroundTintList(getResources().getColorStateList(R.color.lighter_blue));
                btnSolo.setTextColor(getResources().getColor(R.color.light_blue));
            }
        });


        placeRecyclerView = findViewById(R.id.placeRecyclerView);
        placeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        placeList = new ArrayList<>();
        placeList.add(new Place("https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcQSXSYMy134UZwQw2ZFN-nCLwjZInk4mr6azYQVFwwEImkHNEpzSJ1Rb8aTVdGkUI7sgQ4TdU0gX-qcGLnI1AEKvWMJWL2-T2wz5npdoQ", "Saintmartin", 4.0f));
        placeList.add(new Place("https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcQSXSYMy134UZwQw2ZFN-nCLwjZInk4mr6azYQVFwwEImkHNEpzSJ1Rb8aTVdGkUI7sgQ4TdU0gX-qcGLnI1AEKvWMJWL2-T2wz5npdoQ", "Bandarban", 4.0f));
        placeList.add(new Place("https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcQSXSYMy134UZwQw2ZFN-nCLwjZInk4mr6azYQVFwwEImkHNEpzSJ1Rb8aTVdGkUI7sgQ4TdU0gX-qcGLnI1AEKvWMJWL2-T2wz5npdoQ", "Bandarban", 4.0f));
        placeList.add(new Place("https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcQSXSYMy134UZwQw2ZFN-nCLwjZInk4mr6azYQVFwwEImkHNEpzSJ1Rb8aTVdGkUI7sgQ4TdU0gX-qcGLnI1AEKvWMJWL2-T2wz5npdoQ", "Bandarban", 4.0f));

        placeAdapter = new PlaceAdapter(this, placeList);
        placeRecyclerView.setAdapter(placeAdapter);
        //-------------------------------------------------------
        setupBottomNavigation(R.id.nav_home);
        //-------------------------------------------------------------------
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceList = new ArrayList<>();
        serviceList.add(new Service(R.drawable.ic_safety, "Safety Insured", "There are many variations of passages Lorem."));
        serviceList.add(new Service(R.drawable.ic_sponsor, "Become a Sponsor", "There are many variations of passages Lorem."));
        serviceList.add(new Service(R.drawable.ic_support, "24X7 Help & Support", "There are many variations of passages Lorem."));

        serviceAdapter = new ServiceAdapter(this, serviceList);
        servicesRecyclerView.setAdapter(serviceAdapter);
    }

    @Override
    public void onCategoryClick(int position) {
        for (Category category : categoryList) {
            category.setSelected(false);
        }
        categoryList.get(position).setSelected(true);
        categoryAdapter.notifyDataSetChanged();
    }
}