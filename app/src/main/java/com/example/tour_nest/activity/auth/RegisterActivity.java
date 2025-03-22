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

        if (fullName.isEmpty()) {
            binding.layoutFullName.setError("Vui lòng nhập họ và tên");
            return;
        } else {
            binding.layoutFullName.setError(null);
        }

        if (email.isEmpty()) {
            binding.layoutEmail.setError("Vui lòng nhập email");
            return;
        } else if (!Common.isValidEmail(email)) {
            binding.layoutEmail.setError("Email không hợp lệ");
            return;
        } else {
            binding.layoutEmail.setError(null);
        }

        if (phone.isEmpty()) {
            binding.layoutPhone.setError("Vui lòng nhập số điện thoại");
            return;
        } else {
            binding.layoutPhone.setError(null);
        }

        if (password.isEmpty()) {
            binding.layoutPassword.setError("Vui lòng nhập mật khẩu");
            return;
        } else {
            binding.layoutPassword.setError(null);
        }

        if (confirmPassword.isEmpty()) {
            binding.layoutConfirmPassword.setError("Vui lòng nhập lại mật khẩu");
            return;
        } else if (!password.equals(confirmPassword)) {
            binding.layoutConfirmPassword.setError("Mật khẩu không khớp");
            return;
        } else {
            binding.layoutConfirmPassword.setError(null);
        }

        if (!binding.chkAgree.isChecked()) {
            Toast.makeText(this, "Bạn cần đồng ý với điều khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(fullName, email, phone, password, "", Common.getCurrentDateByMillis());
        user.setRole(Constant.USER_ROLE);
        user.setStatus(1);
        UserService.register(user).onResult(new FirebaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Toast.makeText(getBaseContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getBaseContext(), "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}