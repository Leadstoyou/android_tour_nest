package com.example.tour_nest.activity.admin.category;

import android.content.Intent;
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

public class EditCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        EditText editTextCategoryName = findViewById(R.id.editTextCategoryName);
        Button btnSaveCategory = findViewById(R.id.btnSaveCategory);

        String categoryId = getIntent().getStringExtra("categoryId");
        String categoryName = getIntent().getStringExtra("categoryName");
        editTextCategoryName.setText(categoryName);

        btnSaveCategory.setOnClickListener(v -> {
            String newName = editTextCategoryName.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }
            CategoryService.getRef().update(categoryId,new Category(newName));
            setResult(RESULT_OK);
            finish();

        });
    }
}