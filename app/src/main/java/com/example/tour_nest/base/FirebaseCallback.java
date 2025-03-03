package com.example.tour_nest.base;

public interface FirebaseCallback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}