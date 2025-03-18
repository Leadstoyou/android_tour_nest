package com.example.tour_nest.activity.admin.tour;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.admin.TourListAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.service.TourService;
import com.example.tour_nest.util.Common;
import com.example.tour_nest.util.LoadingUtil;
import com.example.tour_nest.util.LogUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class AdminTourListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private TourListAdapter tourListAdapter;
    private List<Tour> tourList;
    private ActivityResultLauncher<Intent> addTourLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_tour_list);

        addTourLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadTourData();
                    }
                }
        );

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        ImageButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminTourListActivity.this, AddNewTourActivity.class);
            addTourLauncher.launch(intent);
        });

        toolbar.setClickable(false);
        toolbar.setFocusable(false);

        recyclerView = findViewById(R.id.tour_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tourList = new ArrayList<>();
        tourListAdapter = new TourListAdapter(tourList);
        recyclerView.setAdapter(tourListAdapter);

        loadTourData();
    }

    private void loadTourData() {
        LoadingUtil.showLoading(this);
        tourList.clear();
        TourService.getAllTour().onResult(new FirebaseCallback<List<Tour>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(List<Tour> tours) {
                LogUtil.logMessage(tours.toString());
                tourList.addAll(tours);
                tourListAdapter.notifyDataSetChanged();
                LoadingUtil.hideLoading(AdminTourListActivity.this);
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("Error fetching tours: " + e.getMessage());
                Toast.makeText(AdminTourListActivity.this, "Failed to load tours", Toast.LENGTH_SHORT).show();
                LoadingUtil.hideLoading(AdminTourListActivity.this);
            }
        });
    }
}