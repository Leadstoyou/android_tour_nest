package com.example.tour_nest.adapter.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.activity.favourite.TourListActivity;
import com.example.tour_nest.model.home.CategorySlider;
import com.example.tour_nest.util.Constant;

import java.util.List;

public class CategorySliderAdapter extends RecyclerView.Adapter<CategorySliderAdapter.ViewHolder> {
    private List<CategorySlider> categoryList;
    private Context context;

    public CategorySliderAdapter(Context context, List<CategorySlider> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_slider_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategorySlider category = categoryList.get(position);
        holder.categoryName.setText(category.getName() != null ? category.getName() : "" );
        holder.categoryImage.setImageResource(category.getImageResId());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);

                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String category = categoryName.getText().toString();
                        Context context = v.getContext();
                        Intent intent = new Intent(context, TourListActivity.class);
                        switch (category) {
                            case Constant.NORTH_REGION:
                                intent.putExtra("region", Constant.NORTH_REGION);
                                break;
                            case Constant.CENTRAL_REGION:
                                intent.putExtra("region", Constant.CENTRAL_REGION);
                                break;
                            case Constant.SOUTH_REGION:
                                intent.putExtra("region", Constant.SOUTH_REGION);
                                break;
                            default:
                                Toast.makeText(v.getContext(), "Không xác định!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        context.startActivity(intent);
                    }
                });
            }
    }
}
