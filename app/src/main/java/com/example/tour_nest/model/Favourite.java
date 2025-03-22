package com.example.tour_nest.model;

import java.util.List;

public class Favourite {
    private String id;
    private List<String> favouriteTours;

    public Favourite() {
    }
    public Favourite( List<String> favouriteTours) {
        this.favouriteTours = favouriteTours;
    }
    public Favourite(String id, List<String> favouriteTours) {
        this.id = id;
        this.favouriteTours = favouriteTours;
    }

    @Override
    public String toString() {
        return "Favourite{" +
                "id='" + id + '\'' +
                ", favouriteTours=" + favouriteTours +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getFavouriteTours() {
        return favouriteTours;
    }

    public void setFavouriteTours(List<String> favouriteTours) {
        this.favouriteTours = favouriteTours;
    }
}
