package com.example.tour_nest.activity.admin.category;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
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
import com.example.tour_nest.util.LoadingUtil;
import com.example.tour_nest.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminManageCategoriesActivity extends BaseActivity {

    private RecyclerView categoryRecyclerView;
    private List<Category> originalCategoryList = new ArrayList<>(); // Danh sách gốc từ server
    private CategoryAdapter categoryAdapter;
    private static final int ADD_CATEGORY_REQUEST = 1;
    private static final int EDIT_CATEGORY_REQUEST = 2; // Request code cho edit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_categories);

        EditText searchCategoryEditText = findViewById(R.id.searchCategoryEditText);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), this::editCategory, this::deleteCategoryWithConfirmation);
        categoryRecyclerView.setAdapter(categoryAdapter);

        loadCategories();

        btnAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(AdminManageCategoriesActivity.this, AddCategoryActivity.class);
            startActivityForResult(intent, ADD_CATEGORY_REQUEST);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_CATEGORY_REQUEST || requestCode == EDIT_CATEGORY_REQUEST) && resultCode == RESULT_OK) {
            System.out.println("requestCode == ADD_CATEGORY_REQUEST || requestCode == EDIT_CATEGORY_REQUEST) && resultCode == RESULT_OK");
            loadCategories();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingUtil.removeLoading(AdminManageCategoriesActivity.this);
    }

    private void loadCategories() {
        LoadingUtil.showLoading(AdminManageCategoriesActivity.this);
        CategoryService.getAll().onResult(new FirebaseCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                originalCategoryList.clear();
                originalCategoryList.addAll(result);
                categoryAdapter.updateList(result);
                LoadingUtil.hideLoading(AdminManageCategoriesActivity.this);
                LogUtil.logMessage("Loaded categories :: " + result);
            }

            @Override
            public void onFailure(Exception e) {
                LoadingUtil.hideLoading(AdminManageCategoriesActivity.this);
                Toast.makeText(AdminManageCategoriesActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editCategory(Category category) {
        Intent intent = new Intent(AdminManageCategoriesActivity.this, EditCategoryActivity.class);
        intent.putExtra("categoryId", category.getId());
        intent.putExtra("categoryName", category.getName());
        startActivityForResult(intent, EDIT_CATEGORY_REQUEST);
    }

    private void deleteCategoryWithConfirmation(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa danh mục '" + category.getName() + "' không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    CategoryService.delete(category.getId());
                    loadCategories();

                })
                .setNegativeButton("Không", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterCategories(String query) {
        List<Category> filteredList = new ArrayList<>();
        if (query.isEmpty() || query.isBlank()) {
            categoryAdapter.updateList(originalCategoryList);
            LogUtil.logMessage("Restored original list :: " + originalCategoryList);
            return;
        }
        for (Category category : originalCategoryList) {
            if (category.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(category);
            }
        }
        LogUtil.logMessage("Filtered list :: " + filteredList);
        categoryAdapter.updateList(filteredList);
    }
}