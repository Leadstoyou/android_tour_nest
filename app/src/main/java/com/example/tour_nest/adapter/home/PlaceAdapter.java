package com.example.tour_nest.adapter.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tour_nest.R;
import com.example.tour_nest.activity.home.TourDetailActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Favourite;
import com.example.tour_nest.model.User;
import com.example.tour_nest.model.home.Place;
import com.example.tour_nest.service.FavouriteService;
import com.example.tour_nest.util.SharedPrefHelper;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    private List<Place> placeList;
    private Context context;
    private String userId;

    public PlaceAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
        User user = SharedPrefHelper.getUser(context);
        this.userId = (user != null) ? user.getId() : null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_item, parent, false);
        return new ViewHolder(view, userId);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.placeName.setText(place.getName());
        Picasso.get().load(place.getImageUrl()).into(holder.placeImage);
        holder.tourId = place.getTourId();

        // Hiển thị rating
        for (int i = 0; i < 5; i++) {
            ImageView star = (ImageView) holder.ratingLayout.getChildAt(i);
            if (i < Math.floor(place.getRating())) {
                star.setImageResource(R.drawable.ic_star);
            }
        }

        // Cập nhật trạng thái bookmark ban đầu
        if (userId != null) {
            holder.updateBookmarkState();
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
        String tourId;
        String userId;

        public ViewHolder(@NonNull View itemView, String userId) {
            super(itemView);
            this.userId = userId;
            placeImage = itemView.findViewById(R.id.placeImage);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);
            placeName = itemView.findViewById(R.id.placeName);
            ratingLayout = itemView.findViewById(R.id.ratingLayout);

            bookmarkIcon.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && userId != null) {
                    FavouriteService<Boolean> service = FavouriteService.create(tourId, userId);
                    service.onResult(new FirebaseCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean wasAdded) {
                            bookmarkIcon.setImageResource(
                                    wasAdded ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark
                            );
                        }

                        @Override
                        public void onFailure(Exception e) {
                            // Xử lý lỗi (ví dụ: Toast thông báo)
                        }
                    });
                }
            });

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

        private void updateBookmarkState() {
            FavouriteService.getFavouriteByIdRealtime(userId, new FirebaseCallback<Favourite>() {
                @Override
                public void onSuccess(Favourite result) {
                    if (result != null && result.getFavouriteTours() != null) {
                        boolean isFavorited = result.getFavouriteTours().contains(tourId);
                        bookmarkIcon.setImageResource(
                                isFavorited ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark
                        );
                    } else {
                        bookmarkIcon.setImageResource(R.drawable.ic_bookmark);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    bookmarkIcon.setImageResource(R.drawable.ic_bookmark);
                }
            });
        }
    }
}