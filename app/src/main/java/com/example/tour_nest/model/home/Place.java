package com.example.tour_nest.model.home;

public class Place {
    private String imageUrl;
    private String name;
    private float rating;

    public Place(String imageUrl, String name, float rating) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.rating = rating;
    }

    public String getImageUrl() { return imageUrl; }
    public String getName() { return name; }
    public float getRating() { return rating; }
}