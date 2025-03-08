package com.example.tour_nest.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tour_nest.R;
import com.example.tour_nest.activity.favourite.TourListActivity;
import com.example.tour_nest.activity.home.HomeActivity;
import com.example.tour_nest.activity.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
                        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }
    protected void setupBottomNavigation(@IdRes int selectedItemId) {
        bottomNavigationView = findViewById(com.example.tour_nest.R.id.bottomNavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == selectedItemId) return true;

                Class<?> targetActivity;
                if (item.getItemId() == R.id.nav_home) {
                    targetActivity = HomeActivity.class;
                } else if (item.getItemId() == R.id.nav_favorite) {
                    targetActivity = TourListActivity.class;
                } else {
                    targetActivity = ProfileActivity.class;
                }

                startActivity(new Intent(this, targetActivity));
                overridePendingTransition(0, 0);
                finish();
                return true;
            });
        }
    }
}