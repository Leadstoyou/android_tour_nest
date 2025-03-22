package com.example.tour_nest.service;

import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.base.GenericRepository;
import com.example.tour_nest.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class CategoryService<T> {
    private static final GenericRepository<Category> categoryRepository = new GenericRepository<>("categories", Category.class);
    private FirebaseCallback<T> callback;

    public CategoryService() {
    }

    public void onResult(FirebaseCallback<T> callback) {
        this.callback = callback;
    }
    public static GenericRepository<Category> getRef(){
        return categoryRepository;
    }
    public static CategoryService<Category> create(Category category) {
        CategoryService<Category> service = new CategoryService<>();
        if (category == null || category.getName() == null || category.getName().isEmpty()) {
            if (service.callback != null) {
                service.callback.onFailure(new Exception("Danh mục không hợp lệ"));
            }
            return service;
        }

        categoryRepository.search("name", category.getName(), new FirebaseCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                if (result.isEmpty()) {
                    categoryRepository.create(category);
                    if (service.callback != null) {
                        service.callback.onSuccess(category);
                    }
                } else {
                    if (service.callback != null) {
                        service.callback.onFailure(new Exception("Danh mục đã tồn tại"));
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) {
                    service.callback.onFailure(e);
                }
            }
        });
        return service;
    }

    public static CategoryService<Category> getById(String id) {
        CategoryService<Category> service = new CategoryService<>();
        if (id == null || id.isEmpty()) {
            if (service.callback != null) {
                service.callback.onFailure(new Exception("ID danh mục không hợp lệ"));
            }
            return service;
        }

        categoryRepository.get(id, new FirebaseCallback<Category>() {
            @Override
            public void onSuccess(Category result) {
                if (result != null) {
                    if (service.callback != null) {
                        service.callback.onSuccess(result);
                    }
                } else {
                    if (service.callback != null) {
                        service.callback.onFailure(new Exception("Không tìm thấy danh mục"));
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) {
                    service.callback.onFailure(e);
                }
            }
        });
        return service;
    }

    public static CategoryService<List<Category>> getAll() {
        CategoryService<List<Category>> service = new CategoryService<>();
        categoryRepository.getAll(new FirebaseCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                if (service.callback != null) {
                    service.callback.onSuccess(result);
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) {
                    service.callback.onFailure(e);
                }
            }
        });
        return service;
    }


    public static CategoryService<Boolean> delete(String id) {
        CategoryService<Boolean> service = new CategoryService<>();
        if (id == null || id.isEmpty()) {
            if (service.callback != null) {
                service.callback.onFailure(new Exception("ID danh mục không hợp lệ"));
            }
            return service;
        }

        categoryRepository.delete(id);
        if (service.callback != null) {
            service.callback.onSuccess(true); // Trả về true ngay lập tức sau khi gọi delete
        }
        return service;
    }


}