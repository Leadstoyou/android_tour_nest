package com.example.tour_nest.model.tour;

public class FavoriteItem {
    private String id;



    private String imageUrl;
    private String packageName;
    private String rating;

    public FavoriteItem(String id, String imageUrl, String packageName, String rating) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.packageName = packageName;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FavoriteItem{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", packageName='" + packageName + '\'' +
                ", rating='" + rating + '\'' +
                '}';
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
