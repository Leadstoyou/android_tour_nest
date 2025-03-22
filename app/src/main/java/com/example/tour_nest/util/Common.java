package com.example.tour_nest.util;



public class Common {

    public static String getCurrentDateByMillis() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
    public static boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailPattern);
    }

}