package com.example.tour_nest.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.profile.OrderAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityOrderListBinding;
import com.example.tour_nest.model.Order;
import com.example.tour_nest.model.User;
import com.example.tour_nest.service.OrderService;
import com.example.tour_nest.util.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseActivity {
    private ActivityOrderListBinding binding;
    private OrderAdapter adapter;
    private List<Order> orderList = new ArrayList<>();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy userId từ SharedPrefHelper
        User user = SharedPrefHelper.getUser(this);
        userId = (user != null) ? user.getId() : null;

        if (userId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem danh sách đơn đặt tour", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Thiết lập Toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Orders");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Thiết lập RecyclerView
        RecyclerView recyclerView = binding.recyclerViewOrders;
        adapter = new OrderAdapter(this, orderList);
        recyclerView.setAdapter(adapter);

        // Tải danh sách Order
        loadOrders();
    }

    private void loadOrders() {
        OrderService.getByUserId(userId).onResult(new FirebaseCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                if (result != null && !result.isEmpty()) {
                    orderList.clear();
                    orderList.addAll(result);
                    adapter.setOrderList(orderList);
                    binding.tvEmptyList.setVisibility(View.GONE);
                } else {
                    binding.tvEmptyList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(OrderListActivity.this, "Lỗi khi tải danh sách: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.tvEmptyList.setVisibility(View.VISIBLE);
            }
        });
    }
}