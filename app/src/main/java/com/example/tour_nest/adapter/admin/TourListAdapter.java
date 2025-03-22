package com.example.tour_nest.adapter.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.model.Tour;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.function.Consumer;

public class TourListAdapter extends RecyclerView.Adapter<TourListAdapter.TourViewHolder> {
    private List<Tour> tourList;
    private Consumer<Tour> onEditClick;
    private Consumer<Tour> onDeleteClick;

    public TourListAdapter(List<Tour> tourList, Consumer<Tour> onEditClick, Consumer<Tour> onDeleteClick) {
        this.tourList = tourList;
        this.onEditClick = onEditClick;
        this.onDeleteClick = onDeleteClick;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_tour_list, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);

        holder.tourName.setText(tour.getName() != null ? tour.getName() : "Không có tên");
        holder.tourDescription.setText(tour.getDescription() != null ? tour.getDescription() : "Không có mô tả");

        Picasso.get()
                .load(tour.getThumbnail())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.tourThumbnail);

        holder.editButton.setOnClickListener(v -> {
            if (onEditClick != null) {
                onEditClick.accept(tour);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClick != null) {
                onDeleteClick.accept(tour);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourList != null ? tourList.size() : 0;
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView tourThumbnail;
        TextView tourName;
        TextView tourDescription;
        Button editButton;
        Button deleteButton;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            tourThumbnail = itemView.findViewById(R.id.tour_thumbnail);
            tourName = itemView.findViewById(R.id.tour_name);
            tourDescription = itemView.findViewById(R.id.tour_description);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}