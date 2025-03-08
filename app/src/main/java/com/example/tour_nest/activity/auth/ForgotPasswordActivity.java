package com.example.tour_nest.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityForgotPasswordBinding;
import com.example.tour_nest.service.UserService;
import com.example.tour_nest.util.LogUtil;

import java.util.Objects;

public class ForgotPasswordActivity extends BaseActivity {
    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        UserService.resetPassword(email).onResult(new FirebaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Toast.makeText(getBaseContext(), "Email đặt lại mật khẩu đã được gửi!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Email không tồn tại trong hệ thông", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                LogUtil.logMessage("resetPassword :: " + e.getMessage());
                Toast.makeText(getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
