package com.example.tour_nest.model.home;

public class TourPackage {
    private String id;
    private String imageUrl;
    private String title;
    private String location;
    private float rating;

    public TourPackage(String id, String imageUrl, String title, String location, float rating) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.location = location;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImageUrl() { return imageUrl; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public float getRating() { return rating; }

    @Override
    public String toString() {
        return "TourPackage{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", rating=" + rating +
                '}';
    }
}
