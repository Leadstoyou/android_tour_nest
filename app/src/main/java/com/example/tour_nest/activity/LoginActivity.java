package com.example.tour_nest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tour_nest.MainActivity;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityLoginBinding;
import com.example.tour_nest.model.User;
import com.example.tour_nest.service.UserService;
import com.example.tour_nest.util.SharedPrefHelper;

import java.util.Objects;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.edtEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
            new UserService(new User(email , password))
                    .login()
                    .onResult(new FirebaseCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {
                            if(result){
                                Toast.makeText(getBaseContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(getBaseContext(), "Tài khoản không đã tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getBaseContext(), "Đăng nhập thât bại!", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        binding.btnFacebook.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng nhập Facebook!", Toast.LENGTH_SHORT).show();
        });

        binding.btnGoogle.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng nhập Google!", Toast.LENGTH_SHORT).show();
        });
    }
}