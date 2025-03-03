package com.example.tour_nest.service;

import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.base.GenericRepository;
import com.example.tour_nest.model.User;

import java.util.List;

public class UserService {
    private static final GenericRepository<User> userRepository = new GenericRepository<>("users", User.class);
    private final User user;
    private FirebaseCallback<Boolean> callback;

    public UserService(User user) {
        this.user = user;
    }

    // Register method using Builder pattern
    public UserService register() {
        userRepository.search("email", user.getEmail(), new FirebaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (result.isEmpty()) {
                    userRepository.create(user);
                    if (callback != null) callback.onSuccess(true);
                } else {
                    if (callback != null) callback.onSuccess(false);
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (callback != null) callback.onFailure(e);
            }
        });
        return this;
    }

    // Login method using Builder pattern
    public UserService login() {
        userRepository.search("email", user.getEmail(), new FirebaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (!result.isEmpty() && result.get(0).getPassword().equals(user.getPassword())) {
                    if (callback != null) callback.onSuccess(true);
                } else {
                    if (callback != null) callback.onSuccess(false);
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (callback != null) callback.onFailure(e);
            }
        });
        return this;
    }

    // Set callback using method chaining
    public UserService onResult(FirebaseCallback<Boolean> callback) {
        this.callback = callback;
        return this;
    }
}
