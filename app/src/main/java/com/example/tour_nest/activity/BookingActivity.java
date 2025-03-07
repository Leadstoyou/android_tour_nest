package com.example.tour_nest.activity;

import android.os.Bundle;

import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.databinding.ActivityBookingBinding;

public class BookingActivity extends BaseActivity {
    private ActivityBookingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}