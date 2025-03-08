package com.example.tour_nest.util;

import com.google.firebase.auth.FirebaseAuth;

public class EmailSender {
    public static void sendResetPasswordEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        LogUtil.logMessage("Email đặt lại mật khẩu đã được gửi!");
                    } else {
                        LogUtil.logMessage("Gửi email thất bại!" );
                    }
                });
    }
}
