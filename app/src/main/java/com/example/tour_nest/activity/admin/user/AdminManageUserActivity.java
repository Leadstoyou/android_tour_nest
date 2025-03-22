package com.example.tour_nest.activity.admin.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.admin.UserAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.User;
import com.example.tour_nest.service.UserService;
import com.example.tour_nest.util.LoadingUtil;
import com.example.tour_nest.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminManageUserActivity extends BaseActivity {
    private RecyclerView userRecyclerView;
    private List<User> userList = new ArrayList<>();
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_user);

        EditText searchUserEditText = findViewById(R.id.searchUserEditText);
        userRecyclerView = findViewById(R.id.userRecyclerView);

        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userList, this::toggleUserStatus);
        userRecyclerView.setAdapter(userAdapter);

        loadUsers();

        searchUserEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterUsers(s.toString());
            }
        });
    }

    private void loadUsers() {
        LoadingUtil.showLoading(this);

        UserService.getAllUsers().onResult(new FirebaseCallback<List<User>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(List<User> result) {
                if (!isFinishing() && !isDestroyed()) {
                    userList.clear();
                    userList.addAll(result);
                    userAdapter.updateList(userList);
                    userAdapter.notifyDataSetChanged();
                }
                LoadingUtil.hideLoading(AdminManageUserActivity.this); // Ẩn ProgressBar
            }

            @Override
            public void onFailure(Exception e) {
                LogUtil.logMessage("loadUsers :: Tải dữ liệu thất bại - " + e.getMessage());
                if (!isFinishing() && !isDestroyed()) {
                    Toast.makeText(AdminManageUserActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                LoadingUtil.hideLoading(AdminManageUserActivity.this); // Ẩn ProgressBar
            }
        });
    }

    private void toggleUserStatus(User user) {
        int newStatus = user.getStatus() == 1 ? 0 : 1;
        user.setStatus(newStatus);

        LoadingUtil.showLoading(this);

        UserService.update(user.getId(), user);
        Toast.makeText(AdminManageUserActivity.this,
                newStatus == 1 ? "Mở tài khoản thành công" : "Khóa tài khoản thành công",
                Toast.LENGTH_SHORT).show();
        loadUsers();
    }

    private void filterUsers(String query) {
        if (query.isEmpty()) {
            userAdapter.updateList(userList);
            return;
        }

        List<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            if (user.getFullName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        userAdapter.updateList(filteredList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingUtil.removeLoading(this); // Xóa ProgressBar khi Activity hủy (tùy chọn)
    }
}