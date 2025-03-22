package com.example.tour_nest.activity.message;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.home.MessageAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Message;
import com.example.tour_nest.service.MessageService;
import com.example.tour_nest.util.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserChatActivity extends BaseActivity {
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private String currentUserId;
    private final String adminId = "-OKnvAJWX2CH1OSAvsyY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        currentUserId = Objects.requireNonNull(SharedPrefHelper.getUser(this)).getId();
        // Khởi tạo UI
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        // Thiết lập RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList, currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);

        // Lắng nghe cuộc hội thoại theo thời gian thực
        loadConversation();

        // Xử lý sự kiện gửi tin nhắn
        buttonSend.setOnClickListener(v -> {
            String content = editTextMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                sendMessage(content);
                editTextMessage.setText(""); // Xóa nội dung sau khi gửi
            } else {
                Toast.makeText(this, "Vui lòng nhập tin nhắn!", Toast.LENGTH_SHORT).show();
            }
        });
        setupBottomNavigation(R.id.nav_message);
    }

    private void loadConversation() {
        MessageService<List<Message>> service = MessageService.getConversationRealtime(currentUserId, adminId);
        service.onResult(new FirebaseCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> conversation) {
                messageList.clear();
                messageList.addAll(conversation);
                messageAdapter.updateMessages(messageList);
                recyclerViewMessages.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(UserChatActivity.this, "Lỗi tải tin nhắn: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String content) {
        Message message = new Message(currentUserId, adminId, content, System.currentTimeMillis());
        MessageService.sendMessage(message);
        // Tin nhắn mới sẽ tự động cập nhật qua realtime từ Firebase
    }
}