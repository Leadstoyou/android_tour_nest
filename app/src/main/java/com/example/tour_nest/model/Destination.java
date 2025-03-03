package com.example.tour_nest.model;

public class Destination {
    private String name;
    private int imageResId;

    public Destination(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
