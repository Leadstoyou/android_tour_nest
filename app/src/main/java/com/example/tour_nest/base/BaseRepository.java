package com.example.tour_nest.base;

import java.util.List;

public interface BaseRepository<T> {
    void create(T item);

    void createWithKey(String key,T item);

    void update(String id, T item);

    void delete(String id);

    void get(String id, FirebaseCallback<T> callback);

    void getRealtimeById(String id,FirebaseCallback<T> callback );

    void getAll(FirebaseCallback<List<T>> callback);

    void search(String field, String value, FirebaseCallback<List<T>> callback);

    void searchRealtime(String field, String value, FirebaseCallback<List<T>> callback);

    void getByIds(List<String> ids, FirebaseCallback<List<T>> callback);
}