package com.example.tour_nest.adapter.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tour_nest.R;
import com.example.tour_nest.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private final Consumer<User> onToggleStatusClick;

    public UserAdapter(List<User> userList, Consumer<User> onToggleStatusClick) {
        this.userList = userList != null ? userList : new ArrayList<>();
        this.onToggleStatusClick = onToggleStatusClick;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);

        holder.btnToggleStatus.setOnClickListener(v -> {
            if (onToggleStatusClick != null) {
                onToggleStatusClick.accept(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(List<User> newList) {
        this.userList =newList; // Cập nhật danh sách thay vì chỉ clear & addAll
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivUserImage;
        private final TextView tvFullName;
        private final TextView tvEmail;
        private final TextView tvStatus;
        private final Button btnToggleStatus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnToggleStatus = itemView.findViewById(R.id.btnToggleStatus);
        }

        public void bind(User user) {
            tvFullName.setText(user.getFullName());
            tvEmail.setText(user.getEmail());
            tvStatus.setText(user.getStatus() == 1 ? "Hoạt động" : "Khóa");
            btnToggleStatus.setText(user.getStatus() == 1 ? "Khóa" : "Mở");
            btnToggleStatus.setBackgroundTintList(user.getStatus() == 1 ?
                    itemView.getContext().getResources().getColorStateList(android.R.color.holo_red_light) :
                    itemView.getContext().getResources().getColorStateList(android.R.color.holo_green_light));

            Glide.with(itemView.getContext())
                    .load(user.getUserImage())
                    .placeholder(R.drawable.placeholder_image) // Thêm drawable placeholder nếu cần
                    .into(ivUserImage);
        }
    }
}