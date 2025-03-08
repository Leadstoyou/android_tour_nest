package com.example.tour_nest.activity.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.databinding.ActivityResetPasswordBinding;

import java.util.Objects;

public class ResetPasswordActivity extends BaseActivity {
    private com.example.tour_nest.databinding.ActivityResetPasswordBinding binding; // ViewBinding instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndResetPassword();
            }
        });
    }

    private void validateAndResetPassword() {
        String currentPassword = Objects.requireNonNull(binding.editCurrentPassword.getText()).toString().trim();
        String newPassword = Objects.requireNonNull(binding.editNewPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.editConfirmPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
    }
}