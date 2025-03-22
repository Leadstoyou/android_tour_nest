package com.example.tour_nest.util;

import java.util.Arrays;
import java.util.List;

public class Constant {
    public static final String NORTH_REGION = "Miền Bắc";
    public static final String CENTRAL_REGION = "Miền Trung";
    public static final String SOUTH_REGION = "Miền Nam";
    public static final String ORDER_STATUS_ALL = "All";
    public static final String ORDER_STATUS_PENDING = "Pending";
    public static final String ORDER_STATUS_CONFIRMED = "Confirmed";
    public static final String ORDER_STATUS_CANCELLED = "Cancelled";
    public static final String ORDER_STATUS_COMPLETED = "Completed";

    // Danh sách các trạng thái để sử dụng trong Spinner hoặc logic khác
    public static final List<String> ORDER_STATUS_LIST = Arrays.asList(
            ORDER_STATUS_ALL,
            ORDER_STATUS_PENDING,
            ORDER_STATUS_CONFIRMED,
            ORDER_STATUS_CANCELLED,
            ORDER_STATUS_COMPLETED
    );
}
