package com.example.tour_nest.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tour_nest.R;
import com.example.tour_nest.activity.favourite.TourListActivity;
import com.example.tour_nest.activity.home.HomeActivity;
import com.example.tour_nest.activity.message.UserChatActivity;
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }


    }
    protected void setupBottomNavigation(@IdRes int selectedItemId) {
        bottomNavigationView = findViewById(com.example.tour_nest.R.id.bottomNavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == selectedItemId) return true; // Đã ở Activity này rồi, không làm gì cả

                Intent intent = null;
                if (item.getItemId() == R.id.nav_home) {
                    intent = new Intent(this, HomeActivity.class);
                } else if (item.getItemId() == R.id.nav_favorite) {
                    if (!(this instanceof TourListActivity)) {
                        intent = new Intent(this, TourListActivity.class);
                    }
                } else if (item.getItemId() == R.id.nav_message) {
                    intent = new Intent(this, UserChatActivity.class);
                } else {
                    intent = new Intent(this, ProfileActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                return true;
            });
        }
    }
}