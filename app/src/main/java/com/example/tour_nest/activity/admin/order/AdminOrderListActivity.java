package com.example.tour_nest.activity.admin.order;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.admin.AdminOrderAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityAdminOrderListBinding;
import com.example.tour_nest.model.Order;
import com.example.tour_nest.service.OrderService;
import com.example.tour_nest.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderListActivity extends BaseActivity {
    private ActivityAdminOrderListBinding binding;
    private AdminOrderAdapter adapter;
    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminOrderListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Admin - All Orders");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = binding.recyclerViewOrders;
        adapter = new AdminOrderAdapter(this, orderList);
        recyclerView.setAdapter(adapter);

        loadAllOrders();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constant.ORDER_STATUS_LIST) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTypeface(null, Typeface.BOLD);
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup    parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTypeface(null, Typeface.BOLD); 
                return textView;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStatus.setAdapter(spinnerAdapter);
        binding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = parent.getItemAtPosition(position).toString();
                adapter.sortByStatus(selectedStatus);
                if (adapter.getItemCount() == 0) {
                    binding.tvEmptyList.setVisibility(View.VISIBLE);
                } else {
                    binding.tvEmptyList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadAllOrders() {
        OrderService.getRef().getAll(new FirebaseCallback<List<Order>>() {
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
                Toast.makeText(AdminOrderListActivity.this, "Lỗi khi tải danh sách: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.tvEmptyList.setVisibility(View.VISIBLE);
            }
        });
    }
}