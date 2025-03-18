package com.example.tour_nest.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.constant.Constant;
import com.example.tour_nest.databinding.ActivityRegisterBinding;
import com.example.tour_nest.model.User;
import com.example.tour_nest.service.UserService;
import com.example.tour_nest.util.Common;

import java.time.Instant;


public class RegisterActivity extends BaseActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(v -> registerUser());

        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String fullName = binding.edtFullName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!binding.chkAgree.isChecked()) {
            Toast.makeText(this, "Bạn cần đồng ý với điều khoản!", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(fullName, email, phone, password, "", Common.getCurrentDateByMillis());
        user.setRole(Constant.USER_ROLE);
        user.setStatus(0);
        UserService
                .register(user)
                .onResult(new FirebaseCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            Toast.makeText(getBaseContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(getBaseContext(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getBaseContext(), "Đăng ký thât bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
