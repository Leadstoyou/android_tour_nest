package com.example.tour_nest.model;

import java.io.Serializable;
import java.util.List;

public class Tour implements Serializable {
    private String id;
    private String name;
    private String description;
    private String thumbnail;
    private List<String> gallery;
    private List<String> placesToVisit;
    private List<String> includedServices;
    private List<String> excludedServices;
    private List<String> cancellationPolicy;
    private List<String> childPolicy;
    private double price;
    private int days;
    private int nights;
    private String departure;
    private String destination;
    private String transportation;
    private String region;

    private int slot;

    private String categoryId;
    private List<String> departureDates;
    public Tour() {
    }

    public Tour(String name, String description, String thumbnail, List<String> gallery, List<String> placesToVisit, List<String> includedServices, List<String> excludedServices, List<String> cancellationPolicy, List<String> childPolicy, double price, int days, int nights, String departure, String destination, String transportation, String region, int slot, String categoryId, List<String> departureDates) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.gallery = gallery;
        this.placesToVisit = placesToVisit;
        this.includedServices = includedServices;
        this.excludedServices = excludedServices;
        this.cancellationPolicy = cancellationPolicy;
        this.childPolicy = childPolicy;
        this.price = price;
        this.days = days;
        this.nights = nights;
        this.departure = departure;
        this.destination = destination;
        this.transportation = transportation;
        this.region = region;
        this.slot = slot;
        this.categoryId = categoryId;
        this.departureDates = departureDates;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", gallery=" + gallery +
                ", placesToVisit=" + placesToVisit +
                ", includedServices=" + includedServices +
                ", excludedServices=" + excludedServices +
                ", cancellationPolicy=" + cancellationPolicy +
                ", childPolicy=" + childPolicy +
                ", price=" + price +
                ", days=" + days +
                ", nights=" + nights +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", transportation='" + transportation + '\'' +
                ", region='" + region + '\'' +
                ", slot=" + slot +
                ", categoryId='" + categoryId + '\'' +
                ", departureDates=" + departureDates +
                '}';
    }

    public List<String> getDepartureDates() {
        return departureDates;
    }

    public void setDepartureDates(List<String> departureDates) {
        this.departureDates = departureDates;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }

    public List<String> getPlacesToVisit() {
        return placesToVisit;
    }

    public void setPlacesToVisit(List<String> placesToVisit) {
        this.placesToVisit = placesToVisit;
    }

    public List<String> getIncludedServices() {
        return includedServices;
    }

    public void setIncludedServices(List<String> includedServices) {
        this.includedServices = includedServices;
    }

    public List<String> getExcludedServices() {
        return excludedServices;
    }

    public void setExcludedServices(List<String> excludedServices) {
        this.excludedServices = excludedServices;
    }

    public List<String> getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(List<String> cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public List<String> getChildPolicy() {
        return childPolicy;
    }

    public void setChildPolicy(List<String> childPolicy) {
        this.childPolicy = childPolicy;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }
}