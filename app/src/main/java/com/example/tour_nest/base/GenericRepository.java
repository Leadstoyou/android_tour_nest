package com.example.tour_nest.base;

import androidx.annotation.NonNull;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class GenericRepository<T> implements BaseRepository<T> {
    private final DatabaseReference databaseReference;
    private final Class<T> modelClass;

    public GenericRepository(String collectionName, Class<T> modelClass) {
        this.databaseReference = FirebaseDatabase.getInstance("https://tour-nest-e1492-default-rtdb.asia-southeast1.firebasedatabase.app").getReference(collectionName);
        this.modelClass = modelClass;
    }

    @Override
    public void create( T data) {
        databaseReference.push().setValue(data);
    }

    @Override
    public void update(String id, T data) {
        databaseReference.child(id).setValue(data);
    }

    @Override
    public void delete(String id) {
        databaseReference.child(id).removeValue();
    }

    @Override
    public void get(String id, FirebaseCallback<T> callback) {
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                T data = snapshot.getValue(modelClass);
                callback.onSuccess(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }

    @Override
    public void getAll(FirebaseCallback<List<T>> callback) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> dataList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    dataList.add(data.getValue(modelClass));
                }
                callback.onSuccess(dataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }
    @Override
    public void search(String field, String value, FirebaseCallback<List<T>> callback) {
        Query query = databaseReference.orderByChild(field).equalTo(value);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> results = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    results.add(data.getValue(modelClass));
                }
                callback.onSuccess(results);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }
}