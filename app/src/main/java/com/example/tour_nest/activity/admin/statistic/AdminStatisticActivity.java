package com.example.tour_nest.activity.admin.statistic;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.admin.PopularTourAdapter;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityAdminStatisticBinding;
import com.example.tour_nest.model.Order;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.model.admin.TourStats;
import com.example.tour_nest.service.OrderService;
import com.example.tour_nest.service.TourService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminStatisticActivity extends AppCompatActivity {

    private TextView tvTotalRevenue, tvOrderCount;
    private BarChart chartRevenue;
    private PieChart chartOrderStatus;
    private RecyclerView rvPopularTours;
    private ActivityAdminStatisticBinding binding;
    private List<Order> orders = new ArrayList<>();
    private List<Tour> tours = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminStatisticBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo các view
        tvTotalRevenue = findViewById(R.id.tv_total_revenue);
        tvOrderCount = findViewById(R.id.tv_order_count);
        chartRevenue = findViewById(R.id.chart_revenue);
        chartOrderStatus = findViewById(R.id.chart_order_status);
        rvPopularTours = findViewById(R.id.rv_popular_tours);
        rvPopularTours.setLayoutManager(new LinearLayoutManager(this));

        loadDataAndUpdateUI();
    }

    private void loadDataAndUpdateUI() {
        OrderService.getRef().getAll(new FirebaseCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                orders.clear();
                orders.addAll(result);

                TourService.getAllTour().onResult(new FirebaseCallback<List<Tour>>() {
                    @Override
                    public void onSuccess(List<Tour> result) {
                        tours.clear();
                        tours.addAll(result);

                        updateDashboard(orders);
                        setupRevenueChart(orders);
                        setupOrderStatusChart(orders);
                        setupPopularTours(tours, orders);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateDashboard(List<Order> orders) {
        double totalRevenue = orders.stream().mapToDouble(Order::getTotal).sum();
        int orderCount = orders.size();

        tvTotalRevenue.setText(String.format("%,.0f VNĐ", totalRevenue));
        tvOrderCount.setText(String.valueOf(orderCount));
    }

    private void setupRevenueChart(List<Order> orders) {
        List<BarEntry> entries = new ArrayList<>();
        Map<String, Double> revenueByMonth = new HashMap<>();
        for (Order order : orders) {
            String month = order.getDepartureDate().substring(0, 7); // Lấy YYYY-MM
            revenueByMonth.put(month, revenueByMonth.getOrDefault(month, 0.0) + order.getTotal());
        }

        int index = 0;
        for (Map.Entry<String, Double> entry : revenueByMonth.entrySet()) {
            entries.add(new BarEntry(index++, entry.getValue().floatValue()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        chartRevenue.setData(new BarData(dataSet));
        chartRevenue.invalidate();
    }

    private void setupOrderStatusChart(List<Order> orders) {
        List<PieEntry> entries = new ArrayList<>();
        Map<String, Integer> statusCount = new HashMap<>();
        for (Order order : orders) {
            statusCount.put(order.getStatus(), statusCount.getOrDefault(order.getStatus(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Trạng thái");
        chartOrderStatus.setData(new PieData(dataSet));
        chartOrderStatus.invalidate();
    }

    private void setupPopularTours(List<Tour> tours, List<Order> orders) {
        Map<String, Integer> tourOrderCount = new HashMap<>();
        Map<String, Double> tourRevenue = new HashMap<>();
        for (Order order : orders) {
            tourOrderCount.put(order.getTourId(), tourOrderCount.getOrDefault(order.getTourId(), 0) + 1);
            tourRevenue.put(order.getTourId(), tourRevenue.getOrDefault(order.getTourId(), 0.0) + order.getTotal());
        }

        List<TourStats> tourStatsList = new ArrayList<>();
        for (Tour tour : tours) {
            int count = tourOrderCount.getOrDefault(tour.getId(), 0);
            double revenue = tourRevenue.getOrDefault(tour.getId(), 0.0);
            if (count > 0) {
                tourStatsList.add(new TourStats(tour.getName(), count, revenue));
            }
        }

        rvPopularTours.setAdapter(new PopularTourAdapter(tourStatsList));
    }
}