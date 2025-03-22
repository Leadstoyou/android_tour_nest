package com.example.tour_nest.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tour_nest.R;
import com.example.tour_nest.activity.auth.LoginActivity;
import com.example.tour_nest.activity.favourite.TourListActivity;
import com.example.tour_nest.adapter.home.CategoryAdapter;
import com.example.tour_nest.adapter.home.CategorySliderAdapter;
import com.example.tour_nest.adapter.home.PlaceAdapter;
import com.example.tour_nest.adapter.home.ServiceAdapter;
import com.example.tour_nest.adapter.home.TourAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Favourite;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.model.User;
import com.example.tour_nest.model.home.Category;
import com.example.tour_nest.model.home.CategorySlider;
import com.example.tour_nest.model.home.Place;
import com.example.tour_nest.model.home.Service;
import com.example.tour_nest.model.home.TourPackage;
import com.example.tour_nest.service.CategoryService;
import com.example.tour_nest.service.FavouriteService;
import com.example.tour_nest.service.TourService;
import com.example.tour_nest.util.Constant;
import com.example.tour_nest.util.LoadingUtil;
import com.example.tour_nest.util.LogUtil;
import com.example.tour_nest.util.SharedPrefHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HomeActivity extends BaseActivity implements CategoryAdapter.OnCategoryClickListener {
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    private ViewPager2 tourSlider;
    private TourAdapter tourAdapter;
    private List<TourPackage> listPackageTour;
    private RecyclerView categorSlideryRecyclerView;
    private CategorySliderAdapter categorySliderAdapter;
    private List<CategorySlider> categorySliderList;

    private MaterialButton btnSolo, btnFamily;

    private RecyclerView placeRecyclerView;
    private PlaceAdapter placeAdapter;

    private RecyclerView servicesRecyclerView;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;

    private List<Tour> listTour = new ArrayList<>();
    private EditText searchInput;
    private Favourite userFavourite = new Favourite();
    private User myUser = new User();
    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tourSlider = findViewById(R.id.tourSlider);
        categorSlideryRecyclerView = findViewById(R.id.categorySliderRecyclerView);
        searchInput = findViewById(R.id.search_input);
        searchInput.setOnEditorActionListener(handleSearch());
        loadAllTour();
        setupBottomNavigation(R.id.nav_home);
    }

    private void loadFavourite() {
        FavouriteService.getFavouriteByIdRealtime(myUser.getId(), new FirebaseCallback<Favourite>() {
            @Override
            public void onSuccess(Favourite result) {
                if (result != null) {
                    Log.d("Realtime", "Danh sách yêu thích: " + result.getFavouriteTours());
                } else {
                    Log.d("Realtime", "Không có danh sách yêu thích cho user này");
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private TextView.OnEditorActionListener handleSearch() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchQuery = v.getText().toString().trim();
                    if (!searchQuery.isEmpty()) {
                        Intent intent = new Intent(HomeActivity.this, TourListActivity.class);
                        intent.putExtra("search", searchQuery);
                        startActivity(intent);
                    }
                    return true;
                } else {
                    if (event != null) {
                        event.getKeyCode();
                    }
                }
                return false;
            }


        };
    }

    private void loadAllTour() {
        if(loadUserData()){
            LoadingUtil.showLoading(HomeActivity.this);
            TourService.getAllTour().onResult(new FirebaseCallback<List<Tour>>() {
                @Override
                public void onSuccess(List<Tour> result) {
                    listTour.addAll(result);
                    loadFavourite();
                    loadPackage();
                    loadCategory();
                    loadRecommendType();
                    loadAppSpecial();
                    LoadingUtil.hideLoading(HomeActivity.this);
                }

                @Override
                public void onFailure(Exception e) {
                    LoadingUtil.hideLoading(HomeActivity.this);
                    Toast.makeText(HomeActivity.this, "Lỗi tải danh sách tour: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
    }

    private boolean loadUserData() {
        myUser = SharedPrefHelper.getUser(getBaseContext());
        if (myUser != null) {
            ((TextView) findViewById(R.id.home_user_name)).setText(myUser.getFullName());
            return true;
        }
        return false;
    }


    private void loadAppSpecial() {
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceList = new ArrayList<>();
        serviceList.add(new Service(R.drawable.ic_safety, "Safety Insured", "There are many variations of passages Lorem."));
        serviceList.add(new Service(R.drawable.ic_sponsor, "Become a Sponsor", "There are many variations of passages Lorem."));
        serviceList.add(new Service(R.drawable.ic_support, "24X7 Help & Support", "There are many variations of passages Lorem."));

        serviceAdapter = new ServiceAdapter(this, serviceList);
        servicesRecyclerView.setAdapter(serviceAdapter);
    }

    private void loadRecommendData(String type) {
        placeRecyclerView = findViewById(R.id.placeRecyclerView);
        placeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Place> placeList = new ArrayList<>();
        switch (type) {
            case "long":
                for (Tour tour : listTour) {
                    if ((tour.getNights() + tour.getDays()) > 5) {
                        LogUtil.logMessage("long" + tour);
                        placeList.add(new Place(tour.getId(),
                                tour.getThumbnail(),
                                tour.getName(),
                                5.0f));
                    }
                }
                break;
            case "short":
                for (Tour tour : listTour) {
                    if ((tour.getNights() + tour.getDays()) <= 5) {
                        LogUtil.logMessage("short" + tour);
                        placeList.add(new Place(tour.getId(),
                                tour.getThumbnail(),
                                tour.getName(),
                                5.0f));
                    }
                }
                break;
        }

        placeAdapter = new PlaceAdapter(this, placeList);
        placeRecyclerView.setAdapter(placeAdapter);
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void loadRecommendType() {
        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroup);

        btnSolo = findViewById(R.id.btnSolo);
        btnFamily = findViewById(R.id.btnFamily);
        loadRecommendData("short");
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.btnSolo) {
                loadRecommendData("short");
                btnSolo.setBackgroundTintList(getResources().getColorStateList(R.color.light_blue));
                btnSolo.setTextColor(getResources().getColor(R.color.white));
                btnFamily.setBackgroundTintList(getResources().getColorStateList(R.color.lighter_blue));
                btnFamily.setTextColor(getResources().getColor(R.color.light_blue));
            } else if (checkedId == R.id.btnFamily) {
                loadRecommendData("long");
                btnFamily.setBackgroundTintList(getResources().getColorStateList(R.color.light_blue));
                btnFamily.setTextColor(getResources().getColor(R.color.white));
                btnSolo.setBackgroundTintList(getResources().getColorStateList(R.color.lighter_blue));
                btnSolo.setTextColor(getResources().getColor(R.color.light_blue));
            }
        });
    }

    private void loadCategory() {
        categorSlideryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        categorySliderList = new ArrayList<>();
        categorySliderList.add(new CategorySlider(R.drawable.ic_mountain, Constant.NORTH_REGION));
        categorySliderList.add(new CategorySlider(R.drawable.ic_beach, Constant.CENTRAL_REGION));
        categorySliderList.add(new CategorySlider(R.drawable.ic_history, Constant.SOUTH_REGION));

        categorySliderAdapter = new CategorySliderAdapter(this, categorySliderList);
        categorSlideryRecyclerView.setAdapter(categorySliderAdapter);
    }

    private void loadPackage() {
        categoryList = new ArrayList<>();
        CategoryService.getAll().onResult(new FirebaseCallback<List<com.example.tour_nest.model.Category>>() {
            @Override
            public void onSuccess(List<com.example.tour_nest.model.Category> result) {
                categoryList.add(new Category("all", "All", true));
                result.forEach(category -> categoryList.add(new Category(category.getId(), category.getName(), false)));
                categoryAdapter = new CategoryAdapter(categoryList, HomeActivity.this);
                categoryRecyclerView.setAdapter(categoryAdapter);
                loadPackageData();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(HomeActivity.this, "Lỗi loadCategory", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPackageData() {
        if (categoryList != null) {
            for (Category category : categoryList) {
                if (category.isSelected()) {
                    listPackageTour = new ArrayList<>();
                    if (category.getId().equals("all")) {
                        listTour.forEach(tour -> listPackageTour.add(new TourPackage(tour.getId(), tour.getThumbnail(), tour.getName(), tour.getRegion(), 5.0f)));
                    } else {
                        listTour.forEach(tour -> {
                            if (tour.getCategoryId().equals(category.getId())) {
                                listPackageTour.add(new TourPackage(tour.getId(), tour.getThumbnail(), tour.getName(), tour.getRegion(), 5.0f));
                            }
                        });
                    }
                    tourAdapter = new TourAdapter(this, listPackageTour);
                    tourSlider.setAdapter(tourAdapter);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingUtil.hideLoading(HomeActivity.this);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCategoryClick(int position) {
        for (Category category : categoryList) {
            category.setSelected(false);
        }
        categoryList.get(position).setSelected(true);
        categoryAdapter.notifyDataSetChanged();
        loadPackageData();
    }
}