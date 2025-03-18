package com.example.tour_nest.activity.admin.category;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.admin.CategoryAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Category;
import com.example.tour_nest.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

public class AdminManageCategoriesActivity extends BaseActivity {

    private RecyclerView categoryRecyclerView;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_categories);

        EditText searchCategoryEditText = findViewById(R.id.searchCategoryEditText);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(categoryList, this::editCategory, this::deleteCategory);
        categoryRecyclerView.setAdapter(categoryAdapter);

        loadCategories();

        btnAddCategory.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến trang thêm danh mục", Toast.LENGTH_SHORT).show();
        });

        searchCategoryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterCategories(s.toString());
            }
        });
    }

    private void loadCategories() {
        CategoryService.getAll().onResult(new FirebaseCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                categoryList.clear();
                categoryList.addAll(result);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminManageCategoriesActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editCategory(Category category) {
        // Chuyển đến Activity chỉnh sửa danh mục
        Toast.makeText(this, "Chỉnh sửa: " + category.getName(), Toast.LENGTH_SHORT).show();
    }

    private void deleteCategory(Category category) {
        CategoryService.delete(category.getId()).onResult(new FirebaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                loadCategories(); // Tải lại danh sách sau khi xóa
                Toast.makeText(AdminManageCategoriesActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminManageCategoriesActivity.this, "Lỗi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterCategories(String query) {
        List<Category> filteredList = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(category);
            }
        }
        categoryAdapter.updateList(filteredList);
    }
}