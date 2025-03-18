package com.example.tour_nest.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.tour_nest.R;
import com.example.tour_nest.activity.admin.category.AdminManageCategoriesActivity;
import com.example.tour_nest.activity.admin.tour.AdminTourListActivity;
import com.example.tour_nest.activity.admin.user.AdminManageUserActivity;
import com.example.tour_nest.activity.auth.LoginActivity;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.util.SharedPrefHelper;

public class AdminDashboardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Khởi tạo các nút
        Button btnManageCategories = findViewById(R.id.btnManageCategories);
        Button btnManageTours = findViewById(R.id.btnManageTours);
        Button btnManageBookings = findViewById(R.id.btnManageBookings);
        Button btnManageUsers = findViewById(R.id.btnManageUsers);
        Button btnReportsAnalytics = findViewById(R.id.btnReportsAnalytics);
        Button btnAdminLogout = findViewById(R.id.btnAdminLogout);

        btnManageCategories.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminManageCategoriesActivity.class);
            startActivity(intent);
        });

        btnManageTours.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminTourListActivity.class);
            startActivity(intent);
        });

//        btnManageBookings.setOnClickListener(v -> {
//            Intent intent = new Intent(AdminDashboardActivity.this, ManageBookingsActivity.class);
//            startActivity(intent);
//        });
//
        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminManageUserActivity.class);
            startActivity(intent);
        });
//
//        btnReportsAnalytics.setOnClickListener(v -> {
//            Intent intent = new Intent(AdminDashboardActivity.this, ReportsAnalyticsActivity.class);
//            startActivity(intent);
//        });

        btnAdminLogout.setOnClickListener(v->{
            SharedPrefHelper.logout(this);
            startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
            });
    }
}