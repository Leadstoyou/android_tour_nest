package com.example.tour_nest.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tour_nest.R;
import com.example.tour_nest.activity.auth.LoginActivity;
import com.example.tour_nest.activity.home.HomeActivity;
import com.example.tour_nest.util.SharedPrefHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SharedPrefHelper.isLoggedIn(getBaseContext())){
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}