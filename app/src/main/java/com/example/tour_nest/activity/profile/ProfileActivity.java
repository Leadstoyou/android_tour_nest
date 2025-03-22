package com.example.tour_nest.activity.profile;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tour_nest.R;
import com.example.tour_nest.adapter.profile.SettingsAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.databinding.ActivityProfileBinding;
import com.example.tour_nest.model.User;
import com.example.tour_nest.model.profile.SettingItem;
import com.example.tour_nest.util.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private SettingsAdapter adapter;
    private List<SettingItem> settingList;

    private ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // RecyclerView Setup
        recyclerView = findViewById(R.id.recyclerViewSettings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        settingList = new ArrayList<>();
        settingList.add(new SettingItem(R.drawable.ic_profile, getString(R.string.setting_your_profile)));
//        settingList.add(new SettingItem(R.drawable.ic_support, getString(R.string.setting_payment_history)));
        settingList.add(new SettingItem(R.drawable.ic_booking, getString(R.string.setting_my_booking)));
        settingList.add(new SettingItem(R.drawable.ic_logout, getString(R.string.setting_log_out)));
        User user = SharedPrefHelper.getUser(getBaseContext());
        if(user != null){
            binding.userName.setText(user.getFullName());
            binding.userEmail.setText(user.getEmail());

            Glide.with(getBaseContext())
                    .load(user.getUserImage())
                    .placeholder(R.drawable.placeholder_image) // Thêm drawable placeholder nếu cần
                    .into(binding.profileImage);
        }
        adapter = new SettingsAdapter(this, settingList);
        recyclerView.setAdapter(adapter);

        setupBottomNavigation(R.id.nav_more);
    }
}
