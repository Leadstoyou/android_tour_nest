package com.example.tour_nest.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

public class LoadingUtil {
    private static ProgressBar progressBar;

    /**
     * Hiển thị ProgressBar bằng cách thêm nó vào root layout của Activity
     * @param activity Activity hiện tại
     */
    public static void showLoading(@NonNull Activity activity) {
        if (!activity.isFinishing() && !activity.isDestroyed()) {
            // Lấy root layout của Activity
            ViewGroup rootView = activity.findViewById(android.R.id.content);
            if (rootView == null) {
                LogUtil.logMessage("LoadingUtil :: Không tìm thấy root layout");
                return;
            }

            // Nếu ProgressBar chưa tồn tại, tạo mới
            if (progressBar == null) {
                progressBar = new ProgressBar(activity);
                progressBar.setId(ViewGroup.generateViewId()); // Tạo ID duy nhất
                progressBar.setLayoutParams(new ViewGroup.LayoutParams(
                        48, // width = 48dp
                        48  // height = 48dp
                ));

                // Căn giữa ProgressBar
                if (rootView instanceof android.widget.FrameLayout) {
                    android.widget.FrameLayout.LayoutParams params =
                            new android.widget.FrameLayout.LayoutParams(48, 48);
                    params.gravity = Gravity.CENTER;
                    progressBar.setLayoutParams(params);
                }

                LogUtil.logMessage("LoadingUtil :: Tạo mới ProgressBar");
            }

            // Nếu ProgressBar chưa được thêm vào layout, thêm nó
            if (progressBar.getParent() == null) {
                rootView.addView(progressBar);
                LogUtil.logMessage("LoadingUtil :: Thêm ProgressBar vào root layout");
            }

            progressBar.setVisibility(View.VISIBLE);
            LogUtil.logMessage("LoadingUtil :: Hiển thị ProgressBar");
        } else {
            LogUtil.logMessage("LoadingUtil :: Không hiển thị ProgressBar vì Activity đã hủy");
        }
    }

    /**
     * Ẩn ProgressBar
     * @param activity Activity hiện tại
     */
    public static void hideLoading(@NonNull Activity activity) {
        if (!activity.isFinishing() && !activity.isDestroyed()) {
            if (progressBar != null && progressBar.getParent() != null) {
                progressBar.setVisibility(View.GONE);
                LogUtil.logMessage("LoadingUtil :: Ẩn ProgressBar");
            } else {
                LogUtil.logMessage("LoadingUtil :: ProgressBar không tồn tại hoặc chưa được thêm");
            }
        } else {
            LogUtil.logMessage("LoadingUtil :: Không ẩn ProgressBar vì Activity đã hủy");
        }
    }

    /**
     * Xóa ProgressBar khỏi layout khi không cần thiết (tùy chọn)
     * @param activity Activity hiện tại
     */
    public static void removeLoading(@NonNull Activity activity) {
        if (progressBar != null && progressBar.getParent() != null) {
            ((ViewGroup) progressBar.getParent()).removeView(progressBar);
            LogUtil.logMessage("LoadingUtil :: Đã xóa ProgressBar khỏi layout");
            progressBar = null; // Đặt lại để tạo mới khi cần
        }
    }
}