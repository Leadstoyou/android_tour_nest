package com.example.tour_nest;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tour_nest.activity.BookingActivity;
import com.example.tour_nest.activity.HomeActivity;
import com.example.tour_nest.activity.LoginActivity;
import com.example.tour_nest.activity.TourDetailActivity;
import com.example.tour_nest.activity.TourListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, TourListActivity.class));
    }
}