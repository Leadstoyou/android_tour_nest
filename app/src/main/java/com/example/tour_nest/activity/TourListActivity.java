package com.example.tour_nest.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.tour.FavoriteAdapter;
import com.example.tour_nest.model.tour.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

public class TourListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<FavoriteItem> favoriteList = new ArrayList<>();
        favoriteList.add(new FavoriteItem("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRK0jXxOguix7NZWjJquhrxtLPfWyxFl4WZ-g&sl", "Cox's Bazar", "★★★★★"));
        favoriteList.add(new FavoriteItem("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRK0jXxOguix7NZWjJquhrxtLPfWyxFl4WZ-g&s", "Cox's Bazar", "★★★★"));

        FavoriteAdapter adapter = new FavoriteAdapter(this, favoriteList);
        recyclerView.setAdapter(adapter);
    }
}