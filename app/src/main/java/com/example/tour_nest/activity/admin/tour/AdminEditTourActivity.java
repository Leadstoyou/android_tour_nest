package com.example.tour_nest.activity.admin.tour;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.admin.GalleryAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.model.Category;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.service.CategoryService;
import com.example.tour_nest.service.CloudinaryService;
import com.example.tour_nest.service.TourService;
import com.example.tour_nest.util.Common;
import com.example.tour_nest.util.Constant;
import com.example.tour_nest.util.LoadingUtil;
import com.example.tour_nest.util.LogUtil;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminEditTourActivity extends BaseActivity {
    private static final int PICK_THUMBNAIL_REQUEST = 1;
    private static final int PICK_IMAGES_REQUEST = 2;

    private List<Uri> imageUris = new ArrayList<>();
    private GalleryAdapter galleryAdapter;
    private ImageView thumbnailImageView;
    private Uri thumbnailUri;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Spinner regionSpinner;
    private Spinner categorySpinner;
    private List<Category> categoryList = new ArrayList<>();
    private Tour tourToEdit;
    private List<String> departureDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_tour);

        thumbnailImageView = findViewById(R.id.thumbnailImageView);
        Button selectThumbnailButton = findViewById(R.id.selectThumbnailButton);
        Button selectGalleryButton = findViewById(R.id.selectGalleryButton);
        RecyclerView galleryRecyclerView = findViewById(R.id.galleryRecyclerView);
        Button submitButton = findViewById(R.id.submitButton);
        regionSpinner = findViewById(R.id.regionSpinner);
        categorySpinner = findViewById(R.id.categorySpinner);

        // Nhận dữ liệu tour từ Intent
        tourToEdit = (Tour) getIntent().getSerializableExtra("tour");
        if (tourToEdit == null) {
            Toast.makeText(this, "Không tìm thấy tour để chỉnh sửa", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Điền dữ liệu cũ vào các trường
        ((EditText) findViewById(R.id.nameEditText)).setText(tourToEdit.getName());
        ((EditText) findViewById(R.id.descriptionEditText)).setText(tourToEdit.getDescription());
        ((EditText) findViewById(R.id.priceEditText)).setText(String.valueOf(tourToEdit.getPrice()));
        ((EditText) findViewById(R.id.daysEditText)).setText(String.valueOf(tourToEdit.getDays()));
        ((EditText) findViewById(R.id.nightsEditText)).setText(String.valueOf(tourToEdit.getNights()));
        ((EditText) findViewById(R.id.departureEditText)).setText(tourToEdit.getDeparture());
        ((EditText) findViewById(R.id.destinationEditText)).setText(tourToEdit.getDestination());
        ((EditText) findViewById(R.id.transportationEditText)).setText(tourToEdit.getTransportation());
        ((EditText) findViewById(R.id.slotEditText)).setText(String.valueOf(tourToEdit.getSlot()));
        departureDates.addAll(tourToEdit.getDepartureDates());

        // Thiết lập spinner vùng
        List<String> regionList = Arrays.asList(Constant.NORTH_REGION, Constant.CENTRAL_REGION, Constant.SOUTH_REGION);
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regionList);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);
        regionSpinner.setSelection(regionList.indexOf(tourToEdit.getRegion()));

        // Thiết lập gallery
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageUris.addAll(tourToEdit.getGallery().stream().map(Uri::parse).collect(Collectors.toList()));
        galleryAdapter = new GalleryAdapter(imageUris, position -> {
            imageUris.remove(position);
            galleryAdapter.notifyItemRemoved(position);
        });
        galleryRecyclerView.setAdapter(galleryAdapter);

        // Thumbnail: Sử dụng Picasso để load ảnh từ URL
        thumbnailUri = Uri.parse(tourToEdit.getThumbnail());
        Picasso.get()
                .load(tourToEdit.getThumbnail()) // Load trực tiếp từ URL
                .placeholder(R.drawable.placeholder_image) // Ảnh hiển thị khi đang tải
                .error(R.drawable.error_image) // Ảnh hiển thị nếu lỗi
                .into(thumbnailImageView);

        // Các trường động
        setupDynamicFields(tourToEdit);

        selectThumbnailButton.setOnClickListener(v -> openImagePicker(PICK_THUMBNAIL_REQUEST));
        selectGalleryButton.setOnClickListener(v -> openImagePicker(PICK_IMAGES_REQUEST));
        submitButton.setOnClickListener(v -> submitTourData());

        loadCategories();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_THUMBNAIL_REQUEST) {
                thumbnailUri = data.getData();
                if (thumbnailUri != null) {
                    // Load ảnh mới chọn bằng Picasso hoặc setImageURI
                    Picasso.get()
                            .load(thumbnailUri)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .into(thumbnailImageView);
                }
            } else if (requestCode == PICK_IMAGES_REQUEST) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                } else if (data.getData() != null) {
                    imageUris.add(data.getData());
                }
                galleryAdapter.notifyDataSetChanged();
            }
        }
    }

    // Các phương thức khác giữ nguyên
    private void setupDynamicFields(Tour tour) {
        addExistingFields(R.id.placesContainer, tour.getPlacesToVisit(), "Nhập địa điểm");
        addExistingFields(R.id.includedContainer, tour.getIncludedServices(), "Nhập dịch vụ bao gồm");
        addExistingFields(R.id.excludedContainer, tour.getExcludedServices(), "Nhập dịch vụ không bao gồm");
        addExistingFields(R.id.cancellationPolicyContainer, tour.getCancellationPolicy(), "Nhập chính sách hủy tour");
        addExistingFields(R.id.childPolicyContainer, tour.getChildPolicy(), "Nhập chính sách trẻ em");

        findViewById(R.id.addPlaceButton).setOnClickListener(v -> addInputField(R.id.placesContainer, "Nhập địa điểm"));
        findViewById(R.id.addIncludedButton).setOnClickListener(v -> addInputField(R.id.includedContainer, "Nhập dịch vụ bao gồm"));
        findViewById(R.id.addExcludedButton).setOnClickListener(v -> addInputField(R.id.excludedContainer, "Nhập dịch vụ không bao gồm"));
        findViewById(R.id.addCancellationPolicyButton).setOnClickListener(v -> addInputField(R.id.cancellationPolicyContainer, "Nhập chính sách hủy tour"));
        findViewById(R.id.addChildPolicyButton).setOnClickListener(v -> addInputField(R.id.childPolicyContainer, "Nhập chính sách trẻ em"));
        findViewById(R.id.addDepartureDateButton).setOnClickListener(v -> showDatePickerDialog());

        LinearLayout departureDatesContainer = findViewById(R.id.departureDatesContainer);
        updateDepartureDatesDisplay(departureDatesContainer);
    }

    private void addExistingFields(int containerId, List<String> items, String hint) {
        LinearLayout container = findViewById(containerId);
        for (String item : items) {
            LinearLayout inputLayout = new LinearLayout(this);
            inputLayout.setOrientation(LinearLayout.HORIZONTAL);
            inputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            EditText editText = new EditText(this);
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            editText.setHint(hint);
            editText.setText(item);

            Button removeButton = new Button(this);
            removeButton.setText("-");
            removeButton.setOnClickListener(v -> container.removeView(inputLayout));

            inputLayout.addView(editText);
            inputLayout.addView(removeButton);
            container.addView(inputLayout);
        }
    }

    private void openImagePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (requestCode == PICK_IMAGES_REQUEST) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        startActivityForResult(intent, requestCode);
    }

    @SuppressLint("CheckResult")
    private void submitTourData() {
        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString().trim();
        String description = ((EditText) findViewById(R.id.descriptionEditText)).getText().toString().trim();
        String priceStr = ((EditText) findViewById(R.id.priceEditText)).getText().toString().trim();
        String daysStr = ((EditText) findViewById(R.id.daysEditText)).getText().toString().trim();
        String nightsStr = ((EditText) findViewById(R.id.nightsEditText)).getText().toString().trim();
        String departure = ((EditText) findViewById(R.id.departureEditText)).getText().toString().trim();
        String destination = ((EditText) findViewById(R.id.destinationEditText)).getText().toString().trim();
        String transportation = ((EditText) findViewById(R.id.transportationEditText)).getText().toString().trim();
        String region = regionSpinner.getSelectedItem().toString();
        String slotStr = ((EditText) findViewById(R.id.slotEditText)).getText().toString().trim();
        Category selectedCategory = (Category) categorySpinner.getSelectedItem();
        String categoryId = selectedCategory != null ? selectedCategory.getId() : null;

        List<String> placesToVisit = getInputData(findViewById(R.id.placesContainer));
        List<String> includedServices = getInputData(findViewById(R.id.includedContainer));
        List<String> excludedServices = getInputData(findViewById(R.id.excludedContainer));
        List<String> cancellationPolicy = getInputData(findViewById(R.id.cancellationPolicyContainer));
        List<String> childPolicy = getInputData(findViewById(R.id.childPolicyContainer));

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || daysStr.isEmpty() || nightsStr.isEmpty()
                || departure.isEmpty() || destination.isEmpty() || transportation.isEmpty() || region.isEmpty()
                || slotStr.isEmpty() || categoryId == null || placesToVisit.isEmpty() || includedServices.isEmpty()
                || excludedServices.isEmpty() || cancellationPolicy.isEmpty() || childPolicy.isEmpty()
                || thumbnailUri == null || imageUris.isEmpty() || departureDates.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        int days, nights, slot;
        try {
            price = Double.parseDouble(priceStr);
            days = Integer.parseInt(daysStr);
            nights = Integer.parseInt(nightsStr);
            slot = Integer.parseInt(slotStr);
            if (slot <= 0) {
                Toast.makeText(this, "Số lượng chỗ phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá tiền, số ngày, số đêm, số lượng chỗ phải là số hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        CloudinaryService cloudinaryService = new CloudinaryService();

        Single<String> thumbnailUpload = Single.fromCallable(() -> cloudinaryService.uploadImageSync(this, thumbnailUri));
        List<Single<String>> galleryUploads = imageUris.stream()
                .map(uri -> Single.fromCallable(() -> cloudinaryService.uploadImageSync(this, uri)))
                .collect(Collectors.toList());

        LoadingUtil.showLoading(this);

        Disposable disposable = Single.zip(
                        thumbnailUpload,
                        Single.zip(galleryUploads, urls -> {
                            List<String> galleryUrlList = new ArrayList<>();
                            for (Object obj : urls) {
                                galleryUrlList.add(obj.toString());
                            }
                            return galleryUrlList;
                        }),
                        (thumbnailUrl, galleryUrls) -> {
                            Tour updatedTour = new Tour(name, description, thumbnailUrl, galleryUrls, placesToVisit,
                                    includedServices, excludedServices, cancellationPolicy, childPolicy,
                                    price, days, nights, departure, destination, transportation, region, slot, categoryId, departureDates);
                            updatedTour.setId(tourToEdit.getId());
                            TourService.getRef().update(updatedTour.getId(), updatedTour);
                            return true;
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        success -> {
                            LoadingUtil.hideLoading(this);
                            Toast.makeText(this, "Cập nhật tour thành công", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        },
                        error -> {
                            LoadingUtil.hideLoading(this);
                            Toast.makeText(this, "Lỗi cập nhật tour: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    private List<String> getInputData(LinearLayout container) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            LinearLayout inputLayout = (LinearLayout) container.getChildAt(i);
            View firstChild = inputLayout.getChildAt(0);
            String input = "";

            if (firstChild instanceof EditText) {
                input = ((EditText) firstChild).getText().toString().trim();
            } else if (firstChild instanceof TextView) {
                input = ((TextView) firstChild).getText().toString().trim();
            }

            if (!input.isEmpty()) {
                data.add(input);
            }
        }
        return data;
    }

    private void addInputField(int containerId, String hint) {
        LinearLayout container = findViewById(containerId);
        LinearLayout inputLayout = new LinearLayout(this);
        inputLayout.setOrientation(LinearLayout.HORIZONTAL);
        inputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        EditText editText = new EditText(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        editText.setHint(hint);

        Button removeButton = new Button(this);
        removeButton.setText("-");
        removeButton.setOnClickListener(v -> container.removeView(inputLayout));

        inputLayout.addView(editText);
        inputLayout.addView(removeButton);
        container.addView(inputLayout);
    }

    private void loadCategories() {
        CategoryService.getAll().onResult(new FirebaseCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                LogUtil.logMessage("result::" + result.toString());
                categoryList.clear();
                categoryList.addAll(result);
                ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(AdminEditTourActivity.this,
                        android.R.layout.simple_spinner_item, categoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoryAdapter);

                // Chọn category hiện tại của tour
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryList.get(i).getId().equals(tourToEdit.getCategoryId())) {
                        categorySpinner.setSelection(i);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FirebaseError", "Lỗi khi lấy danh mục: " + e.getMessage());
                Toast.makeText(AdminEditTourActivity.this, "Không thể tải danh mục tour", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());

                    if (!departureDates.contains(formattedDate)) {
                        departureDates.add(formattedDate);
                        LinearLayout departureDatesContainer = findViewById(R.id.departureDatesContainer);
                        updateDepartureDatesDisplay(departureDatesContainer);
                    } else {
                        Toast.makeText(this, "Ngày này đã được chọn", Toast.LENGTH_SHORT).show();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateDepartureDatesDisplay(LinearLayout container) {
        container.removeAllViews();

        for (String date : departureDates) {
            LinearLayout dateLayout = new LinearLayout(this);
            dateLayout.setOrientation(LinearLayout.HORIZONTAL);
            dateLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView dateText = new TextView(this);
            dateText.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            dateText.setText(date);
            dateText.setPadding(0, 8, 0, 8);

            Button removeButton = new Button(this);
            removeButton.setText("-");
            removeButton.setOnClickListener(v -> {
                departureDates.remove(date);
                updateDepartureDatesDisplay(container);
            });

            dateLayout.addView(dateText);
            dateLayout.addView(removeButton);
            container.addView(dateLayout);
        }
    }
}