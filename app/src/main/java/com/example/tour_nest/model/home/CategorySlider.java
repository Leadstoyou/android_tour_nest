package com.example.tour_nest.model.home;

public class CategorySlider {
    private int imageResId;
    private String name;

    public CategorySlider(int imageResId, String name) {
        this.imageResId = imageResId;
        this.name = name;
    }

    public int getImageResId() { return imageResId; }
    public String getName() { return name; }
}
