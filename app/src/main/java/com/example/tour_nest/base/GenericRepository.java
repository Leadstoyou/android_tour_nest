package com.example.tour_nest.base;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GenericRepository<T> implements BaseRepository<T> {
    private final DatabaseReference databaseReference;
    private final Class<T> modelClass;

    public GenericRepository(String collectionName, Class<T> modelClass) {
        this.databaseReference = FirebaseDatabase.getInstance("https://tour-nest-e1492-default-rtdb.asia-southeast1.firebasedatabase.app").getReference(collectionName);
        this.modelClass = modelClass;
    }

    public DatabaseReference getReference() {
        return databaseReference;
    }

    @Override
    public void create(T data) {
        String id = databaseReference.push().getKey();

        if (id != null) {
            try {
                Method setIdMethod = modelClass.getMethod("setId", String.class);
                setIdMethod.invoke(data, id);
            } catch (Exception e) {
                Log.e("Firebase", "Lỗi gán ID khi tạo dữ liệu", e);
            }
            databaseReference.child(id).setValue(data);
        } else {
            Log.e("Firebase", "Không thể tạo ID mới cho dữ liệu");
        }
    }

    @Override
    public void createWithKey(String key, T item) {
        if (key != null && !key.trim().isEmpty()) {
            try {
                Method setIdMethod = modelClass.getMethod("setId", String.class);
                setIdMethod.invoke(item, key);

                databaseReference.child(key).setValue(item)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firebase", "Successfully created item with key: " + key);
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firebase", "Failed to create item with key: " + key, e);
                        });
            } catch (Exception e) {
                Log.e("Firebase", "Error setting ID or saving data with custom key", e);
            }
        } else {
            Log.e("Firebase", "Invalid key provided for createWithKey");
        }
    }

    @Override
    public void update(String id, T data) {
        try {
            T clonedData = (T) data.getClass().newInstance();

            for (Field field : data.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals("id")) {
                    field.set(clonedData, field.get(data));
                }
            }

            databaseReference.child(id).setValue(clonedData);
        } catch (Exception e) {
            Log.e("Firebase", "Error cloning data", e);
        }
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
    public void getRealtimeById(String id, FirebaseCallback<T> callback) {
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                T data = snapshot.getValue(modelClass);
                if (data != null) {
                    try {
                        Method setIdMethod = modelClass.getMethod("setId", String.class);
                        setIdMethod.invoke(data, snapshot.getKey());
                    } catch (Exception e) {
                        Log.e("Firebase", "Lỗi gán ID trong getFavouriteRealtime", e);
                    }
                }
                callback.onSuccess(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi khi lắng nghe dữ liệu realtime: " + error.getMessage());
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
                    T item = data.getValue(modelClass);
                    if (item != null) {
                        try {
                            Method setIdMethod = modelClass.getMethod("setId", String.class);
                            setIdMethod.invoke(item, data.getKey());
                        } catch (Exception e) {
                            Log.e("Firebase", "Lỗi gán ID", e);
                        }
                        dataList.add(item);
                    }
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
        String searchValue = value;
        String endValue = searchValue + "\uf8ff";

        Query query = databaseReference.orderByChild(field)
                .startAt(searchValue)
                .endAt(endValue);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> results = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    T item = data.getValue(modelClass);
                    if (item != null) {
                        try {
                            Method setIdMethod = modelClass.getMethod("setId", String.class);
                            setIdMethod.invoke(item, data.getKey());
                        } catch (Exception e) {
                            Log.e("Firebase", "Lỗi gán ID", e);
                        }
                        results.add(item);
                    }
                }
                callback.onSuccess(results);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }
    @Override
    public void searchRealtime(String field, String value, FirebaseCallback<List<T>> callback) {
        String searchValue = value;
        String endValue = searchValue + "\uf8ff";

        Query query = databaseReference.orderByChild(field)
                .startAt(searchValue)
                .endAt(endValue);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> results = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    T item = data.getValue(modelClass);
                    if (item != null) {
                        try {
                            Method setIdMethod = modelClass.getMethod("setId", String.class);
                            setIdMethod.invoke(item, data.getKey());
                        } catch (Exception e) {
                            Log.e("Firebase", "Lỗi gán ID", e);
                        }
                        results.add(item);
                    }
                }
                callback.onSuccess(results);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }
    public void search(String field, int value, FirebaseCallback<List<T>> callback) {
        Query query = databaseReference.orderByChild(field).equalTo(value);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> results = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    T item = data.getValue(modelClass);
                    if (item != null) {
                        try {
                            Method setIdMethod = modelClass.getMethod("setId", String.class);
                            setIdMethod.invoke(item, data.getKey());
                        } catch (Exception e) {
                            Log.e("Firebase", "Lỗi gán ID", e);
                        }
                        results.add(item);
                    }
                }
                callback.onSuccess(results);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }

    @Override
    public void getByIds(List<String> ids, FirebaseCallback<List<T>> callback) {
        if (ids == null || ids.isEmpty()) {
            callback.onSuccess(new ArrayList<>());
            return;
        }

        List<T> results = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(ids.size()); // Đồng bộ hóa các truy vấn

        for (String id : ids) {
            databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    T item = snapshot.getValue(modelClass);
                    if (item != null) {
                        try {
                            Method setIdMethod = modelClass.getMethod("setId", String.class);
                            setIdMethod.invoke(item, snapshot.getKey());
                            synchronized (results) {
                                results.add(item);
                            }
                        } catch (Exception e) {
                            Log.e("Firebase", "Lỗi gán ID trong getByIds", e);
                        }
                    }
                    latch.countDown(); // Giảm đếm sau khi hoàn thành một truy vấn
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Lỗi khi lấy dữ liệu theo ID: " + id, error.toException());
                    latch.countDown(); // Vẫn giảm đếm để không bị treo
                }
            });
        }

        new Thread(() -> {
            try {
                latch.await();
                callback.onSuccess(results);
            } catch (InterruptedException e) {
                Log.e("Firebase", "Lỗi khi chờ kết quả trong getByIds", e);
                callback.onFailure(e);
            }
        }).start();
    }
}