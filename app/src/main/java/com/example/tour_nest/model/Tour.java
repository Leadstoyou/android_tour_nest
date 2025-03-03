package com.example.tour_nest.model;

public class Tour {
    private String title;
    private String departure;
    private String date;
    private String price;
    private int imageResId;

    public Tour(String title, String departure, String date, String price, int imageResId) {
        this.title = title;
        this.departure = departure;
        this.date = date;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
}
