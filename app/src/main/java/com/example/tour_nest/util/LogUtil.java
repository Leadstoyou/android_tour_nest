package com.example.tour_nest.util;

import android.util.Log;

public class LogUtil {
    private static final String DEFAULT_TAG = "tour_nest";

    public static void logMessage(String tag, String message, LogLevel level) {
        if (tag == null || tag.isEmpty()) {
            tag = DEFAULT_TAG;
        }

        switch (level) {
            case DEBUG:
                Log.d(tag, message);
                break;
            case INFO:
                Log.i(tag, message);
                break;
            case WARN:
                Log.w(tag, message);
                break;
            case ERROR:
                Log.e(tag, message);
                break;
            case VERBOSE:
                Log.v(tag, message);
                break;
            default:
                Log.d(tag, message);
        }
    }

    public static void logMessage(String message) {
        logMessage(DEFAULT_TAG, message, LogLevel.DEBUG);
    }
    public static void logMessage(String tag, String message) {
        if (tag == null || tag.isEmpty()) {
            tag = DEFAULT_TAG;
        }

        Log.d(String.valueOf(LogLevel.DEBUG), message);
    }
    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR, VERBOSE
    }
}
