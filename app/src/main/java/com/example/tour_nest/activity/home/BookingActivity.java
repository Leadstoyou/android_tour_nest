package com.example.tour_nest.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.tour_nest.R;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityBookingBinding;
import com.example.tour_nest.model.Order;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.model.User;
import com.example.tour_nest.service.OrderService;
import com.example.tour_nest.util.Common;
import com.example.tour_nest.util.EmailSender;
import com.example.tour_nest.util.SharedPrefHelper;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class BookingActivity extends BaseActivity {
    private ActivityBookingBinding binding;
    private Tour tour;
    private int adultCount = 1;
    private int childCount = 0;
    private String selectedDepartureDate;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User user = SharedPrefHelper.getUser(this);
        userId = (user != null) ? user.getId() : null;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("tour")) {
            tour = (Tour) intent.getSerializableExtra("tour");
            if (tour != null) {
                loadTourData();
                setupDepartureDatesSpinner();
            }
        }

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnIncreaseAdult.setOnClickListener(v -> {
            adultCount++;
            updatePassengerCount();
            updatePaymentSummary();
        });
        binding.btnDecreaseAdult.setOnClickListener(v -> {
            if (adultCount > 1) {
                adultCount--;
                updatePassengerCount();
                updatePaymentSummary();
            }
        });

        binding.btnIncreaseChild.setOnClickListener(v -> {
            childCount++;
            updatePassengerCount();
            updatePaymentSummary();
        });
        binding.btnDecreaseChild.setOnClickListener(v -> {
            if (childCount > 0) {
                childCount--;
                updatePassengerCount();
                updatePaymentSummary();
            }
        });

        binding.btnProceedToCheckout.setOnClickListener(v -> {
            if (userId == null) {
                Toast.makeText(this, "Vui lòng đăng nhập để đặt tour", Toast.LENGTH_SHORT).show();
                return;
            }

            Order order = createOrder();
            showQRCodeDialog(order);

        });

        updatePassengerCount();
        updatePaymentSummary();
    }

    private void showQRCodeDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_qr_code, null);

        ImageView qrCodeImageView = dialogView.findViewById(R.id.qr_code_image);
        EditText emailInput = dialogView.findViewById(R.id.email_input);

        builder.setView(dialogView);
        builder.setTitle("Thanh toán thành công");
        builder.setMessage("Vui lòng điền email chính xác để nhận thông tin đơn hàng:");
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                emailInput.setError("Vui lòng nhập email");
                return;
            } else if (!Common.isValidEmail(email)) {
                emailInput.setError("Email không hợp lệ");
                return;
            } else {
                emailInput.setError(null);
            }
            if (!email.isEmpty()) {
                order.setOrderEmail(email);
                OrderService.create(order).onResult(new FirebaseCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        Toast.makeText(BookingActivity.this, "Email đã được ghi nhận: " + email, Toast.LENGTH_SHORT).show();
                        new Thread(()->{
                            EmailSender.sendOrderConfirmationEmail(email , order);
                        }).start();
                        Intent intent = new Intent(BookingActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(BookingActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    private void loadTourData() {
        binding.tvTitle.setText(tour.getName());
        binding.tvPackage.setText(tour.getName());
        binding.tvDuration.setText("Days : " + tour.getDays() + " Night : " + tour.getNights());
        binding.tvTransport.setText(tour.getTransportation());
        binding.tvDeparture.setText(tour.getDeparture());
        binding.tvDestination.setText(tour.getDestination());
    }

    private void setupDepartureDatesSpinner() {
        if (tour.getDepartureDates() != null && !tour.getDepartureDates().isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, tour.getDepartureDates()) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    textView.setTypeface(null, Typeface.BOLD); // Làm đậm chữ hiển thị chính
                    return textView;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                    textView.setTypeface(null, Typeface.BOLD); // Làm đậm chữ trong dropdown
                    return textView;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerDepartureDates.setAdapter(adapter);

            selectedDepartureDate = tour.getDepartureDates().get(0);
            binding.spinnerDepartureDates.setSelection(0);

            binding.spinnerDepartureDates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDepartureDate = tour.getDepartureDates().get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });
        } else {
            binding.spinnerDepartureDates.setEnabled(false);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, new String[]{"No dates available"}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    textView.setTypeface(null, Typeface.BOLD);
                    return textView;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                    textView.setTypeface(null, Typeface.BOLD);
                    return textView;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerDepartureDates.setAdapter(adapter);
        }
    }

    private void updatePassengerCount() {
        binding.tvAdultCount.setText(String.valueOf(adultCount));
        binding.tvChildCount.setText(String.valueOf(childCount));
    }

    private void updatePaymentSummary() {
        double basePrice = tour.getPrice();
        double adultPrice = basePrice * adultCount;
        double childPrice = basePrice * 0.5 * childCount;
        double subtotal = adultPrice + childPrice;
        double total = subtotal;

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        binding.tvSubtotal.setText(formatter.format(subtotal));
        binding.tvTotal.setText(formatter.format(total));
    }

    private Order createOrder() {
        double basePrice = tour.getPrice();
        double adultPrice = basePrice * adultCount;
        double childPrice = basePrice * 0.5 * childCount;
        double subtotal = adultPrice + childPrice;
        double total = subtotal;

        return new Order(
                null,
                null,
                userId,
                tour.getId(),
                tour.getName(),
                adultCount,
                childCount,
                selectedDepartureDate,
                basePrice,
                adultPrice,
                childPrice,
                subtotal,
                total,
                "Pending",
                null
        );
    }
}