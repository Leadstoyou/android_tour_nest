package com.example.tour_nest.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tour_nest.R;
import com.example.tour_nest.model.home.Place;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    private List<Place> placeList;
    private Context context;

    public PlaceAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.placeName.setText(place.getName());
        Picasso.get().load(place.getImageUrl()).into(holder.placeImage);

        // Xử lý đánh giá sao
        for (int i = 0; i < 5; i++) {
            ImageView star = (ImageView) holder.ratingLayout.getChildAt(i);
            if (i < Math.floor(place.getRating())) {
                star.setImageResource(R.drawable.ic_star);
            }
        }
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImage, bookmarkIcon;
        TextView placeName;
        LinearLayout ratingLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.placeImage);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);
            placeName = itemView.findViewById(R.id.placeName);
            ratingLayout = itemView.findViewById(R.id.ratingLayout);
        }
    }
}
