package com.example.tour_nest.activity.admin.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.home.MessageAdapter;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Message;
import com.example.tour_nest.model.User;
import com.example.tour_nest.service.MessageService;
import com.example.tour_nest.util.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminUserChatActivity extends AppCompatActivity {
    private static final String TAG = "AdminUserChatActivity";
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private ImageButton buttonBack;
    private com.example.tour_nest.adapter.home.MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private String adminId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_chat);

        // Lấy adminId và userId
        User user = SharedPrefHelper.getUser(this);
        adminId = (user != null) ? user.getId() : "-OKnvAJWX2CH1OSAvsyY";
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "Không tìm thấy userId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "Admin ID: " + adminId + ", User ID: " + userId);

        // Khởi tạo UI
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        buttonBack = findViewById(R.id.buttonBack);

        // Thiết lập RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList, adminId);
        recyclerViewMessages.setAdapter(messageAdapter);

        // Load cuộc hội thoại
        loadConversation();

        // Xử lý nút Back
        buttonBack.setOnClickListener(v -> finish());

        // Xử lý gửi tin nhắn
        buttonSend.setOnClickListener(v -> {
            String content = editTextMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                sendMessage(content);
                editTextMessage.setText("");
            } else {
                Toast.makeText(this, "Vui lòng nhập tin nhắn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadConversation() {
        MessageService<List<Message>> service = MessageService.getConversationRealtime(adminId, userId);
        service.onResult(new FirebaseCallback<List<Message>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(List<Message> conversation) {
                Log.d(TAG, "Conversation loaded: " + conversation.size() + " messages");
                messageList.clear();
                messageList.addAll(conversation);
                messageAdapter.notifyDataSetChanged();
                recyclerViewMessages.scrollToPosition(messageList.size() - 1); // Cuộn xuống tin nhắn mới nhất
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error loading conversation: " + e.getMessage());
                Toast.makeText(AdminUserChatActivity.this, "Lỗi tải tin nhắn: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String content) {
        Message message = new Message(adminId, userId, content, System.currentTimeMillis());
        MessageService.sendMessage(message);
        Log.d(TAG, "Message sent: " + content);
        // Tin nhắn mới sẽ tự động cập nhật qua realtime từ Firebase
    }
}