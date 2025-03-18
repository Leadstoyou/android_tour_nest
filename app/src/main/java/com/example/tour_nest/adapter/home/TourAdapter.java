package com.example.tour_nest.adapter.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.activity.home.TourDetailActivity;
import com.example.tour_nest.model.home.TourPackage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {
    private List<TourPackage> tourList;
    private Context context;

    public TourAdapter(Context context, List<TourPackage> tourList) {
        this.context = context;
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.example.tour_nest.R.layout.tour_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TourPackage tour = tourList.get(position);
        holder.tourTitle.setText(tour.getTitle());
        holder.tourLocation.setText(tour.getLocation());
        holder.tourRating.setText(String.valueOf(tour.getRating()));
        holder.tourId = tour.getId();
        Picasso.get().load(tour.getImageUrl()).into(holder.imageTour);

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.itemView.setLayoutParams(layoutParams);
    }


    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTour;
        TextView tourTitle, tourLocation, tourRating;
        String tourId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTour = itemView.findViewById(R.id.imageTour);
            tourTitle = itemView.findViewById(R.id.tourTitle);
            tourLocation = itemView.findViewById(R.id.tourLocation);
            tourRating = itemView.findViewById(R.id.tourRating);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, TourDetailActivity.class);
                    intent.putExtra("tour_id", tourId);
                    context.startActivity(intent);
                }
            });
        }

    }
}
