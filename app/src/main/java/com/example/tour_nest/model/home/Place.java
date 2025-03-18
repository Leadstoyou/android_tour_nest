package com.example.tour_nest.model.home;

public class Place {
    private String tourId;
    private String imageUrl;
    private String name;
    private float rating;

    @Override
    public String toString() {
        return "Place{" +
                "tourId='" + tourId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                '}';
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Place(String tourId, String imageUrl, String name, float rating) {
        this.tourId = tourId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.rating = rating;
    }

    public String getImageUrl() { return imageUrl; }
    public String getName() { return name; }
    public float getRating() { return rating; }


}