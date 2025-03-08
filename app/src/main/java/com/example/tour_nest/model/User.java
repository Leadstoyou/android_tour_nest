package com.example.tour_nest.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String userImage;
    private String createdAt;
    public User(){}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User( String fullName, String email, String phone, String password, String userImage, String createdAt) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userImage = userImage;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", userImage='" + userImage + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}