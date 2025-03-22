package com.example.tour_nest.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Order;
import com.example.tour_nest.service.OrderService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orderList;
    private List<Order> filteredList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public AdminOrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.filteredList = new ArrayList<>(orderList);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = filteredList.get(position);

        holder.tvTourName.setText(order.getTourName());
        holder.tvUserId.setText("User Email: " + order.getOrderEmail());
        holder.tvDepartureDate.setText("Departure: " + order.getDepartureDate());
        holder.tvPassengerCount.setText(String.format("Adults: %d, Children: %d",
                order.getAdultCount(), order.getChildCount()));
        holder.tvTotalPrice.setText(String.format("Total: $%.2f", order.getTotal()));
        holder.tvStatus.setText("Status: " + order.getStatus());

        holder.btnChangeStatus.setOnClickListener(v -> changeStatus(order));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
        this.filteredList = new ArrayList<>(orderList);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(orderList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Order order : orderList) {
                if (order.getTourName().toLowerCase().contains(lowerQuery) ||
                        order.getUserId().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(order);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void sortByStatus(String status) {
        filteredList.clear();
        if (status.equals("All")) {
            filteredList.addAll(orderList);
        } else {
            for (Order order : orderList) {
                if (order.getStatus().equals(status)) {
                    filteredList.add(order);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void changeStatus(Order order) {
        String currentStatus = order.getStatus();
        String newStatus;
        switch (currentStatus) {
            case "Pending":
                newStatus = "Confirmed";
                break;
            case "Confirmed":
                newStatus = "Completed";
                break;
            case "Completed":
                newStatus = "Cancelled";
                break;
            case "Cancelled":
                newStatus = "Pending";
                break;
            default:
                newStatus = "Pending";
        }

        OrderService.updateStatus(order.getOrderId(), newStatus, new FirebaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                order.setStatus(newStatus);
                notifyDataSetChanged();
                Toast.makeText(context, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(context, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvUserId, tvDepartureDate, tvPassengerCount, tvTotalPrice, tvStatus;
        Button btnChangeStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvDepartureDate = itemView.findViewById(R.id.tvDepartureDate);
            tvPassengerCount = itemView.findViewById(R.id.tvPassengerCount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnChangeStatus = itemView.findViewById(R.id.btnChangeStatus);
        }
    }
}