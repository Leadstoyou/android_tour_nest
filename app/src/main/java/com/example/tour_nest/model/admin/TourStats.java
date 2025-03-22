package com.example.tour_nest.model.admin;

public class TourStats {
    String tourName;
    int orderCount;
    double totalRevenue;

    public TourStats() {
    }

    public TourStats(String tourName, int orderCount, double totalRevenue) {
        this.tourName = tourName;
        this.orderCount = orderCount;
        this.totalRevenue = totalRevenue;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    @Override
    public String toString() {
        return "TourStats{" +
                "tourName='" + tourName + '\'' +
                ", orderCount=" + orderCount +
                ", totalRevenue=" + totalRevenue +
                '}';
    }
}
