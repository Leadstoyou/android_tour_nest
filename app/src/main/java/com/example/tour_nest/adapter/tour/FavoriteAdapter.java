package com.example.tour_nest.adapter.tour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tour_nest.R;
import com.example.tour_nest.model.tour.FavoriteItem;

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

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPackage);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);
            textViewPackageName = itemView.findViewById(R.id.textViewPackageName);
            textViewRating = itemView.findViewById(R.id.textViewRating);
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

        Glide.with(context).load(item.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }
}
