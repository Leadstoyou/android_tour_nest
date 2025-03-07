package com.example.tour_nest.model.tour;

public class FavoriteItem {
    private String imageUrl;
    private String packageName;
    private String rating;

    public FavoriteItem(String imageUrl, String packageName, String rating) {
        this.imageUrl = imageUrl;
        this.packageName = packageName;
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getRating() {
        return rating;
    }
}
