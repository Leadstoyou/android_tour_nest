package com.example.tour_nest.adapter.admin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tour_nest.R;
import com.example.tour_nest.model.Message;
import com.example.tour_nest.model.admin.UserChatItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private static final String TAG = "UserListAdapter";
    private List<UserChatItem> userChatItems;
    private final OnUserClickListener onUserClickListener;

    public UserListAdapter(List<UserChatItem> userChatItems, OnUserClickListener onUserClickListener) {
        this.userChatItems = userChatItems;
        this.onUserClickListener = onUserClickListener;
        Log.d(TAG, "Adapter initialized with " + userChatItems.size() + " items");
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_chat, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserChatItem item = userChatItems.get(position);
        holder.textViewUserId.setText(item.getEmail());
        holder.textViewLastMessage.setText(item.getLastMessage().getContent());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());
        holder.textViewTime.setText(sdf.format(new Date(item.getLastMessage().getTimestamp())));

        // Tải avatar
        String avatarUrl = item.getAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_avatar)
                    .error(R.drawable.ic_avatar)
                    .into(holder.imageViewAvatar);
        } else {
            holder.imageViewAvatar.setImageResource(R.drawable.ic_avatar);
        }

        holder.itemView.setOnClickListener(v -> onUserClickListener.onUserClick(item.getUserId()));
    }

    @Override
    public int getItemCount() {
        int count = userChatItems.size();
        Log.d(TAG, "getItemCount: " + count);
        return count;
    }

    public void updateUserList(List<UserChatItem> newItems) {
        this.userChatItems = newItems;
        Log.d(TAG, "updateUserList called with " + newItems.size() + " items");
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserId, textViewLastMessage, textViewTime;
        ImageView imageViewAvatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserId = itemView.findViewById(R.id.textViewUserId);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
        }
    }

    public interface OnUserClickListener {
        void onUserClick(String userId);
    }
}