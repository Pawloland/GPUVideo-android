package com.daasuu.gpuvideoandroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 88888;
    private boolean isPermissionRequestInProgress = false; // Flag to prevent repeated permission requests
    private boolean permissionGrantedToastShown = false; // Flag to prevent spamming Toasts

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.movie_preview).setOnClickListener(v -> {
            PlayerActivity.startActivity(MainActivity.this);
        });
        findViewById(R.id.camera_record).setOnClickListener(v -> {
            CameraSelectActivity.startActivity(MainActivity.this);
        });
        findViewById(R.id.mp4_compose).setOnClickListener(v -> {
            Mp4ComposeActivity.startActivity(MainActivity.this);
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isPermissionRequestInProgress) {
            checkPermission();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        // request camera permission if it has not been granted.
        boolean needStorage = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && // Android 13
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                needStorage) {
            if (!isPermissionRequestInProgress) {
                isPermissionRequestInProgress = true;
                if (needStorage) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        } else {
            isPermissionRequestInProgress = false; // Reset the flag if all permissions are granted
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isPermissionRequestInProgress = false; // Reset the flag after handling the result
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                boolean allGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    if (!permissionGrantedToastShown) {
                        Toast.makeText(MainActivity.this, "permission has been granted.", Toast.LENGTH_SHORT).show();
                        permissionGrantedToastShown = true;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "[WARN] permission is not granted.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
