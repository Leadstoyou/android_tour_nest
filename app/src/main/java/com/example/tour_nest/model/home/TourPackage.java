package com.example.tour_nest.model.home;

public class TourPackage {
    private String imageUrl;
    private String title;
    private String location;
    private float rating;

    public TourPackage(String imageUrl, String title, String location, float rating) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.location = location;
        this.rating = rating;
    }

    public String getImageUrl() { return imageUrl; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public float getRating() { return rating; }
}
