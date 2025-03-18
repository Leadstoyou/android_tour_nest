package com.example.tour_nest.adapter.admin;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.model.Tour;
import com.squareup.picasso.Picasso;
import java.util.List;
public class TourListAdapter extends RecyclerView.Adapter<TourListAdapter.TourViewHolder>{
    private List<Tour> tourList;

    public TourListAdapter(List<Tour> tourList) {
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_tour_list, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);
        holder.tourName.setText(tour.getName());       // Dòng này có thể gây lỗi nếu tourName là null
        holder.tourDescription.setText(tour.getDescription());

        Picasso.get()
                .load(tour.getThumbnail())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.tourThumbnail);
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView tourThumbnail;
        TextView tourName;
        TextView tourDescription;

        public TourViewHolder(View itemView) {
            super(itemView);
            tourThumbnail = itemView.findViewById(R.id.tour_thumbnail);
            tourName = itemView.findViewById(R.id.tour_name);
            tourDescription = itemView.findViewById(R.id.tour_description);
        }
    }
}
