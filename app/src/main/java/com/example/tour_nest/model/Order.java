package com.example.tour_nest.model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private String orderId;            // ID của đơn đặt tour
    private String orderEmail;
    private String userId;             // ID của người dùng đặt tour
    private String tourId;             // ID của tour
    private String tourName;           // Tên tour
    private int adultCount;            // Số người lớn
    private int childCount;            // Số trẻ em
    private String departureDate;      // Ngày khởi hành đã chọn
    private double basePrice;          // Giá cơ bản của tour
    private double adultPrice;         // Tổng giá cho người lớn
    private double childPrice;         // Tổng giá cho trẻ em
    private double subtotal;           // Tổng phụ
    private double total;              // Tổng cộng
    private String status;             // Trạng thái đơn đặt tour (Pending, Confirmed, Cancelled, Completed)
    private Date orderDate;            // Thời gian đặt tour

    // Constructor mặc định (yêu cầu cho Firebase)
    public Order() {
    }
    public String getOrderEmail() {
        return orderEmail;
    }

    public void setOrderEmail(String orderEmail) {
        this.orderEmail = orderEmail;
    }


    public Order(String orderId, String orderEmail, String userId, String tourId, String tourName, int adultCount, int childCount, String departureDate, double basePrice, double adultPrice, double childPrice, double subtotal, double total, String status, Date orderDate) {
        this.orderId = orderId;
        this.orderEmail = orderEmail;
        this.userId = userId;
        this.tourId = tourId;
        this.tourName = tourName;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.departureDate = departureDate;
        this.basePrice = basePrice;
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
        this.subtotal = subtotal;
        this.total = total;
        this.status = status;
        this.orderDate = orderDate;
    }

    public void setId(String id) {
        this.orderId = id;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(double adultPrice) {
        this.adultPrice = adultPrice;
    }

    public double getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(double childPrice) {
        this.childPrice = childPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderEmail='" + orderEmail + '\'' +
                ", userId='" + userId + '\'' +
                ", tourId='" + tourId + '\'' +
                ", tourName='" + tourName + '\'' +
                ", adultCount=" + adultCount +
                ", childCount=" + childCount +
                ", departureDate='" + departureDate + '\'' +
                ", basePrice=" + basePrice +
                ", adultPrice=" + adultPrice +
                ", childPrice=" + childPrice +
                ", subtotal=" + subtotal +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }
}