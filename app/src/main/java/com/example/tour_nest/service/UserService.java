package com.example.tour_nest.service;

import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.base.GenericRepository;
import com.example.tour_nest.constant.Constant;
import com.example.tour_nest.model.User;
import com.example.tour_nest.util.Common;
import com.example.tour_nest.util.EmailSender;
import com.example.tour_nest.util.LogUtil;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

public class UserService<T> {
    private static final GenericRepository<User> userRepository = new GenericRepository<>("users", User.class);
    private FirebaseCallback<T> callback;

    public UserService() {
    }

    public void onResult(FirebaseCallback<T> callback) {
        this.callback = callback;
    }

    public static UserService<Boolean> register(User user) {
        UserService<Boolean> service = new UserService<>();
        userRepository.search("email", user.getEmail(), new FirebaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (result.isEmpty()) {
                    userRepository.create(user);
                    if (service.callback != null) service.callback.onSuccess(true);
                } else {
                    if (service.callback != null) service.callback.onSuccess(false);
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) service.callback.onFailure(e);
            }
        });
        return service;
    }

    public static UserService<User> handleGoogleSignIn(FirebaseUser firebaseUser) {
        UserService<User> service = new UserService<>();
        userRepository.search("email", firebaseUser.getEmail(), new FirebaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (result.isEmpty()) {
                    User newUser = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhoneNumber(),
                            "123", Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString(), Common.getCurrentDateByMillis());
                    newUser.setRole(Constant.USER_ROLE);
                    userRepository.create(newUser);
                    service.callback.onSuccess(newUser);
                } else {
                    service.callback.onSuccess(result.get(0));
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) service.callback.onFailure(e);
            }
        });
        return service;
    }

    public static UserService<User> login(User user) {
        UserService<User> service = new UserService<>();
        userRepository.search("email", user.getEmail(), new FirebaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (!result.isEmpty() && result.get(0).getPassword().equals(user.getPassword())) {
                    if (service.callback != null) service.callback.onSuccess(result.get(0));
                } else {
                    if (service.callback != null)
                        service.callback.onFailure(new Exception("Sai email hoặc mật khẩu"));
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) service.callback.onFailure(e);
            }
        });
        return service;
    }

    public static UserService<Boolean> resetPassword(String email) {
        UserService<Boolean> service = new UserService<>();
        userRepository.search("email", email, new FirebaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (!result.isEmpty()) {
                    User user = result.get(0);
                    user.setPassword("123@123a");
                    new Thread(() -> {
                        EmailSender.sendResetPasswordEmail(email);
                    }).start();
                    userRepository.update(user.getId(),user);
                    if (service.callback != null) service.callback.onSuccess(true);
                } else {
                    if (service.callback != null) service.callback.onSuccess(false);
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) service.callback.onFailure(e);
            }
        });
        return service;
    }

    public static UserService<List<User>> getAllUsers() {
        UserService<List<User>> service = new UserService<>();
        userRepository.getAll(new FirebaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (service.callback != null) service.callback.onSuccess(result);
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) service.callback.onFailure(e);
            }
        });
        return service;
    }
    public static UserService<User> update(String id, User user) {
        UserService<User> service = new UserService<>();
        if (id == null || id.isEmpty() || user == null) {
            if (service.callback != null) {
                service.callback.onFailure(new Exception("Dữ liệu người dùng không hợp lệ"));
            }
            return service;
        }

        user.setId(id);
        userRepository.update(id, user);
        if (service.callback != null) {
            service.callback.onSuccess(user);
        }
        return service;
    }

}
