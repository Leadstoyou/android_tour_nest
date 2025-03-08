package com.example.tour_nest.model.profile;

public class SettingItem {
    private int icon;
    private String title;

    public SettingItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
