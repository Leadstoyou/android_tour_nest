package com.example.tour_nest.activity.admin.tour;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.admin.TourListAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.service.TourService;
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
    private ActivityResultLauncher<Intent> editTourLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_tour_list);

        // Launcher cho Add
        addTourLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadTourData();
                    }
                }
        );

        // Launcher cho Edit
        editTourLauncher = registerForActivityResult(
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
        tourListAdapter = new TourListAdapter(tourList, this::editTour, this::deleteTourWithConfirmation);
        recyclerView.setAdapter(tourListAdapter);

        loadTourData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingUtil.removeLoading(AdminTourListActivity.this);
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
                Toast.makeText(AdminTourListActivity.this, "Failed to load tours", Toast.LENGTH_SHORT).show();
                LoadingUtil.hideLoading(AdminTourListActivity.this);
            }
        });
    }

    private void editTour(Tour tour) {
        Intent intent = new Intent(AdminTourListActivity.this, AdminEditTourActivity.class);
        intent.putExtra("tour", tour);
        editTourLauncher.launch(intent);
    }

    private void deleteTourWithConfirmation(Tour tour) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa tour '" + tour.getName() + "' không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    LoadingUtil.showLoading(this);
                    TourService.getRef().delete(tour.getId());

                    loadTourData();
                    Toast.makeText(AdminTourListActivity.this, "Xóa tour thành công", Toast.LENGTH_SHORT).show();
                    LoadingUtil.hideLoading(AdminTourListActivity.this);

                })
                .setNegativeButton("Không", null)
                .show();
    }
}