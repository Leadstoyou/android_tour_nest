package com.example.tour_nest.service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Map;

public class CloudinaryService {
    private static final String CLOUD_NAME = "dotknkcep";
    private static final String API_KEY = "913912193751455";
    private static final String API_SECRET = "kNCk1PXVMDdIPs975kW1UpnJrZw";
    private static final String TAG = "CloudinaryService";
    private static final String FOLDER_TAG = "tour_nest";

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        ));
    }

    public void uploadImage(Context context, Uri imageUri, String folderName, CloudinaryCallback callback) {
        new UploadTask(context, cloudinary, folderName, callback).execute(imageUri);
    }

    public String uploadImageSync(Context context, Uri imageUri) {
        try {
            File file = createTempFileFromUri(context, imageUri);
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", FOLDER_TAG));
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class UploadTask extends AsyncTask<Uri, Void, String> {
        private final Context context;
        private final Cloudinary cloudinary;
        private final CloudinaryCallback callback;
        private final String folderName;

        public UploadTask(Context context, Cloudinary cloudinary, String folderName, CloudinaryCallback callback) {
            this.context = context;
            this.cloudinary = cloudinary;
            this.folderName = folderName;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Uri... uris) {
            Uri imageUri = uris[0];
            try {
                File file = createTempFileFromUri(context, imageUri);
                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", folderName));
                return (String) uploadResult.get("secure_url");
            } catch (IOException e) {
                Log.e(TAG, "Upload error: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String url) {
            if (url != null) {
                callback.onSuccess(url);
            } else {
                callback.onError("Upload thất bại");
            }
        }
    }

    private static File createTempFileFromUri(Context context, Uri uri) throws IOException {
        File file = File.createTempFile("upload", ".jpg", context.getCacheDir());
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        return file;
    }

    public interface CloudinaryCallback {
        void onSuccess(String imageUrl);
        void onError(String errorMessage);
    }
}
