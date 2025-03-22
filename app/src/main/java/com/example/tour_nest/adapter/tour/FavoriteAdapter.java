package com.example.tour_nest.adapter.tour;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tour_nest.R;
import com.example.tour_nest.activity.home.TourDetailActivity;
import com.example.tour_nest.model.tour.FavoriteItem;
import com.example.tour_nest.util.LogUtil;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private List<FavoriteItem> favoriteList;

    public FavoriteAdapter(Context context, List<FavoriteItem> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, bookmarkIcon;
        TextView textViewPackageName, textViewRating;
        String tourId;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPackage);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);
            textViewPackageName = itemView.findViewById(R.id.textViewPackageName);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            itemView.setOnClickListener(v->{
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, TourDetailActivity.class);
                    LogUtil.logMessage("tourId :: " + tourId);
                    intent.putExtra("tour_id", tourId);
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteItem item = favoriteList.get(position);
        holder.textViewPackageName.setText(item.getPackageName());
        holder.textViewRating.setText(item.getRating());
        holder.tourId = item.getId();
        Glide.with(context).load(item.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }
}
