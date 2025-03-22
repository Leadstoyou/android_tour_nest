package com.example.tour_nest.adapter.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.model.admin.TourStats;

import java.util.List;

public class PopularTourAdapter extends RecyclerView.Adapter<PopularTourAdapter.ViewHolder> {
    private List<TourStats> tourStatsList;

    public PopularTourAdapter(List<TourStats> tourStatsList) {
        this.tourStatsList = tourStatsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.example.tour_nest.R.layout.item_popular_tour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TourStats stats = tourStatsList.get(position);
        holder.tvTourName.setText(stats.getTourName());
        holder.tvOrderCount.setText(stats.getOrderCount() + " đơn");
        holder.tvTotalRevenue.setText(String.format("%,.0f VNĐ", stats.getTotalRevenue()));
    }

    @Override
    public int getItemCount() {
        return tourStatsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvOrderCount, tvTotalRevenue;

        ViewHolder(View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tv_tour_name);
            tvOrderCount = itemView.findViewById(R.id.tv_order_count);
            tvTotalRevenue = itemView.findViewById(R.id.tv_total_revenue);
        }
    }
}