package com.app.updater;

import android.net.Uri;

/**
 * Created by Pravesh Sharma on 06-07-2018.
 */

public interface DownloadBroadcastListener {
    void onDownloadCompleted(long refId,String msg,Uri uri);

    void onDownloadRunning(boolean isRunning);
    void onDownloadPaused(boolean isPaused);
    void onDownloadPending(boolean isPending);
    void onDownloadError(String reason);

}
