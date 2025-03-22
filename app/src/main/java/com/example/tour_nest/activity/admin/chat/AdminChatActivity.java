package com.example.tour_nest.activity.admin.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.admin.UserListAdapter;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Message;
import com.example.tour_nest.model.User;
import com.example.tour_nest.model.admin.UserChatItem;
import com.example.tour_nest.service.MessageService;
import com.example.tour_nest.service.UserService;
import com.example.tour_nest.util.LoadingUtil;
import com.example.tour_nest.util.SharedPrefHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminChatActivity extends AppCompatActivity implements UserListAdapter.OnUserClickListener {
    private static final String TAG = "AdminChatActivity";
    private RecyclerView recyclerViewUsers;
    private ImageButton buttonBack;
    private BottomNavigationView bottomNavigationView;
    private UserListAdapter userListAdapter;
    private List<UserChatItem> userChatItems = new ArrayList<>();
    private String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);

        User user = SharedPrefHelper.getUser(this);
        adminId = (user != null) ? user.getId() : "-OKnvAJWX2CH1OSAvsyY";

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        buttonBack = findViewById(R.id.buttonBack);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userListAdapter = new UserListAdapter(userChatItems, this);
        recyclerViewUsers.setAdapter(userListAdapter);

        loadUserList();

        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadUserList() {
        MessageService<List<String>> userService = MessageService.getUserListForAdmin(adminId);
        LoadingUtil.showLoading(AdminChatActivity.this);
        userService.onResult(new FirebaseCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> userIds) {
                Log.d(TAG, "User IDs loaded: " + userIds.size());
                if (userIds.isEmpty()) {
                    Toast.makeText(AdminChatActivity.this, "Không có user nào để hiển thị", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<UserChatItem> tempItems = new ArrayList<>();
                AtomicInteger completedRequests = new AtomicInteger(0);

                for (String userId : userIds) {
                    UserService.getRef().get(userId, new FirebaseCallback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            String email = user != null ? user.getEmail() : "Unknown Email";
                            String avatarUrl = user != null ? user.getUserImage() : null;

                            MessageService<List<Message>> convService = MessageService.getConversationRealtime(adminId, userId);
                            convService.onResult(new FirebaseCallback<List<Message>>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onSuccess(List<Message> conversation) {
                                    if (!conversation.isEmpty()) {
                                        Message lastMessage = conversation.stream()
                                                .max(Comparator.comparingLong(Message::getTimestamp))
                                                .orElse(null);
                                        if (lastMessage != null) {
                                            synchronized (tempItems) {
                                                tempItems.add(new UserChatItem(userId, email, lastMessage, avatarUrl));
                                                Log.d(TAG, "Added UserChatItem: " + userId + " - " + email + " - " + lastMessage.getContent());
                                            }
                                        }
                                    }
                                    if (completedRequests.incrementAndGet() == userIds.size()) {
                                        LoadingUtil.hideLoading(AdminChatActivity.this);
                                        Log.d(TAG, "All requests completed. Updating UI with " + tempItems.size() + " items");
                                        updateUI(tempItems);
                                    }
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.e(TAG, "Error loading conversation: " + e.getMessage());
                                    if (completedRequests.incrementAndGet() == userIds.size()) {
                                        LoadingUtil.hideLoading(AdminChatActivity.this);
                                        updateUI(tempItems);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.e(TAG, "Error loading user info: " + e.getMessage());
                            if (completedRequests.incrementAndGet() == userIds.size()) {
                                LoadingUtil.hideLoading(AdminChatActivity.this);
                                updateUI(tempItems);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error loading user list: " + e.getMessage());
                LoadingUtil.hideLoading(AdminChatActivity.this);
                Toast.makeText(AdminChatActivity.this, "Lỗi tải danh sách user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingUtil.removeLoading(AdminChatActivity.this);
    }

    private void updateUI(List<UserChatItem> tempItems) {
        tempItems.sort((item1, item2) -> Long.compare(item2.getLastMessage().getTimestamp(), item1.getLastMessage().getTimestamp()));
        userChatItems.clear();
        userChatItems.addAll(tempItems);
        Log.d(TAG, "UI updated with " + userChatItems.size() + " items");
        userListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserClick(String userId) {
        Intent intent = new Intent(this, AdminUserChatActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}