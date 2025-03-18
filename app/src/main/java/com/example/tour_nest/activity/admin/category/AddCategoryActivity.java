package com.example.tour_nest.activity.admin.category;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tour_nest.R;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Category;
import com.example.tour_nest.service.CategoryService;

public class AddCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        EditText editTextCategoryName = findViewById(R.id.editTextCategoryName);
        Button btnSaveCategory = findViewById(R.id.btnSaveCategory);

        btnSaveCategory.setOnClickListener(v -> {
            String name = editTextCategoryName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }

            CategoryService.create(new Category(name)).onResult(new FirebaseCallback<Category>() {
                @Override
                public void onSuccess(Category result) {
                    Toast.makeText(AddCategoryActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(AddCategoryActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}