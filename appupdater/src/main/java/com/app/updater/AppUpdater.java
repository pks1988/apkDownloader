package com.app.updater;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Pravesh Sharma on 20-06-2018.
 */

public class AppUpdater implements ActivityCompat.OnRequestPermissionsResultCallback, DownloadBroadcastListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 202;
    private String testUrl = "";
    private Uri uri;
    private Context mActivity;
    private String fileName = "";
    private long refId;
    private DownloadBroadcast downloadBroadcast;
    private DownloadManager manager;


    public AppUpdater(Context mActivity, String testUrl) {
        this.testUrl = testUrl;
        this.mActivity = mActivity;
        fileName = "test" + ".apk";
    }


    public void downloadApk() {
        checkPermission();
    }


    private void startDownload() {
        manager = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(testUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(true);
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setTitle("Down");
        request.setDescription("New apk is downloading");
        request.setDestinationUri(createDestinationUri());
        refId = manager != null ? manager.enqueue(request) : 0;

        registerBroadcast();


    }




    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            startDownload();
        }
    }


    public void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        downloadBroadcast = new DownloadBroadcast(refId, mActivity, this,manager);
        mActivity.registerReceiver(downloadBroadcast, filter);
    }

    public void unregisterBroadcast() {
//        mActivity.unregisterReceiver(downloadBroadcast);
    }


    private Uri createDestinationUri() {
        File destination = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        if (!destination.exists()) {
            final boolean mkdirs = destination.mkdirs();
            Log.e("Destination created", String.valueOf(mkdirs));
        }

        String filePath = destination + File.separator + fileName;
        File file = new File(filePath);
        if (file.getParentFile().exists()) {
            boolean result = false;
            result = file.getParentFile().delete();
            if (result) {
                Toast.makeText(mActivity, "file deleted", Toast.LENGTH_LONG).show();
            }
        }
        return Uri.fromFile(file);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    @Override
    public void onDownloadCompleted(long refId, String msg, Uri uri) {
        try {
            manager.openDownloadedFile(refId);
        } catch (FileNotFoundException e) {
           Log.getStackTraceString(e);
        }
    }

    @Override
    public void onDownloadRunning(boolean isRunning) {

    }

    @Override
    public void onDownloadPaused(boolean isPaused) {

    }

    @Override
    public void onDownloadPending(boolean isPending) {

    }

    @Override
    public void onDownloadError(String reason) {

    }
}