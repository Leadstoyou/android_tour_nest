package com.example.tour_nest.activity.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tour_nest.R;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.databinding.ActivityProfileDetailsBinding;

public class ProfileDetailsActivity extends BaseActivity {
    private ActivityProfileDetailsBinding binding;
    private boolean isEdited = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle Back Button
        binding.btnBack.setOnClickListener(v -> finish());

        // Handle Edit Profile Button
        binding.btnSaveChanges.setEnabled(false);
        binding.btnSaveChanges.setBackgroundColor(getResources().getColor(R.color.grey_btn)); // Grey when disabled

        // Listen for text changes
        setupTextChangeListeners();

        // Handle Save Changes Button
        binding.btnSaveChanges.setOnClickListener(v -> {
            if (isEdited) {
                saveProfileChanges();
            }
        });


    }
    private void setupTextChangeListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enableSaveButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        // Attach listener to all editable fields
        binding.userName.addTextChangedListener(textWatcher);
        binding.userPhone.addTextChangedListener(textWatcher);
        binding.userDOB.addTextChangedListener(textWatcher);
    }
    private void enableSaveButton() {
        isEdited = true;
        binding.btnSaveChanges.setEnabled(true);
        binding.btnSaveChanges.setBackgroundColor(getResources().getColor(R.color.blue)); // Change to blue when enabled
    }

    private void saveProfileChanges() {
        String updatedName = binding.userName.getText().toString().trim();
        String updatedPhone = binding.userPhone.getText().toString().trim();
        String updatedDOB = binding.userDOB.getText().toString().trim();

        // Save the updated data (e.g., in SharedPreferences, Database, or API)
        // TODO: Implement saving logic

        isEdited = false;
        binding.btnSaveChanges.setEnabled(false);
        binding.btnSaveChanges.setBackgroundColor(getResources().getColor(R.color.grey_btn));
    }
}