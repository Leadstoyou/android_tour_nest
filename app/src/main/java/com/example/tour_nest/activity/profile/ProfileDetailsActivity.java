package com.example.tour_nest.activity.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.tour_nest.R;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.databinding.ActivityProfileDetailsBinding;
import com.example.tour_nest.model.User;
import com.example.tour_nest.service.CloudinaryService;
import com.example.tour_nest.service.UserService;
import com.example.tour_nest.util.SharedPrefHelper;
import com.squareup.picasso.Picasso;

public class ProfileDetailsActivity extends BaseActivity {
    private ActivityProfileDetailsBinding binding;
    private boolean isEdited = false;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private CloudinaryService cloudinaryService;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cloudinaryService = new CloudinaryService();

        User user = SharedPrefHelper.getUser(getApplication());
        if (user != null) {
            binding.userName.setText(user.getFullName());
            binding.userPhone.setText(user.getPhone());
            binding.userEmail.setText(user.getEmail());
            if (user.getUserImage() != null && !user.getUserImage().isEmpty()) {
                Picasso.get()
                        .load(user.getUserImage())
                        .placeholder(R.drawable.ic_sponsor)
                        .into(binding.profileImage);
            }
        }

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSaveChanges.setEnabled(false);
        binding.btnSaveChanges.setBackgroundColor(getResources().getColor(R.color.grey_btn));

        binding.profileImage.setOnClickListener(v -> openImagePicker());

        setupTextChangeListeners();

        binding.btnSaveChanges.setOnClickListener(v -> {
            if (isEdited || selectedImageUri != null) {
                saveProfileChanges(user);
            }
        });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            Picasso.get()
                                    .load(selectedImageUri)
                                    .placeholder(R.drawable.ic_sponsor)
                                    .into(binding.profileImage);
                            enableSaveButton();
                        }
                    }
                }
        );
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

        binding.userName.addTextChangedListener(textWatcher);
        binding.userPhone.addTextChangedListener(textWatcher);
    }

    private void enableSaveButton() {
        isEdited = true;
        binding.btnSaveChanges.setEnabled(true);
        binding.btnSaveChanges.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void saveProfileChanges(User user) {
        String updatedName = binding.userName.getText().toString().trim();
        String updatedPhone = binding.userPhone.getText().toString().trim();

        if (updatedName.isEmpty() || updatedPhone.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setFullName(updatedName);
        user.setPhone(updatedPhone);

        if (selectedImageUri != null) {
            cloudinaryService.uploadImage(this, selectedImageUri, "user_avatars", new CloudinaryService.CloudinaryCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    user.setUserImage(imageUrl);
                    SharedPrefHelper.saveUser(getApplication(), user);
                    Toast.makeText(ProfileDetailsActivity.this, "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show();
                    resetSaveButton();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(ProfileDetailsActivity.this, "Lỗi upload ảnh: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            SharedPrefHelper.saveUser(getApplication(), user);
            UserService.getRef().update(user.getId(),user);
            Toast.makeText(this, "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show();
            resetSaveButton();
        }
    }

    private void resetSaveButton() {
        isEdited = false;
        selectedImageUri = null;
        binding.btnSaveChanges.setEnabled(false);
        binding.btnSaveChanges.setBackgroundColor(getResources().getColor(R.color.grey_btn));
    }
}