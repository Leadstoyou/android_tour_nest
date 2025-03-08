package com.example.tour_nest.adapter.profile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tour_nest.R;
import com.example.tour_nest.activity.MainActivity;
import com.example.tour_nest.activity.profile.ProfileDetailsActivity;
import com.example.tour_nest.model.profile.SettingItem;
import com.example.tour_nest.util.SharedPrefHelper;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {
    private Context context;
    private List<SettingItem> settingList;

    public SettingsAdapter(Context context, List<SettingItem> settingList) {
        this.context = context;
        this.settingList = settingList;
    }

    public static class SettingsViewHolder extends RecyclerView.ViewHolder {
        ImageView settingIcon;
        TextView settingText;

        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);
            settingIcon = itemView.findViewById(R.id.settingIcon);
            settingText = itemView.findViewById(R.id.settingText);
        }
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_setting, parent, false);
        return new SettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        SettingItem item = settingList.get(position);
        holder.settingIcon.setImageResource(item.getIcon());
        holder.settingText.setText(item.getTitle());

        holder.itemView.setOnClickListener(v -> {
            if (item.getTitle().equals(context.getString(R.string.setting_your_profile))) {
                Intent intent = new Intent(context, ProfileDetailsActivity.class);
                context.startActivity(intent);
            } else if (item.getTitle().equals(context.getString(R.string.setting_log_out))) {
                SharedPrefHelper.logout(context);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingList.size();
    }
}
