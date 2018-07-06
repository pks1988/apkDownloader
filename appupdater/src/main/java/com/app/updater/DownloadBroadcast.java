package com.app.updater;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Pravesh Sharma on 05-07-2018.
 */

public class DownloadBroadcast extends BroadcastReceiver {

    private final Context mContext;
    private final DownloadManager manager;
    long downLodRefId;
    DownloadBroadcastListener mListener;

    public DownloadBroadcast(long downloadRefId, Context mContext, DownloadBroadcastListener mListener, DownloadManager manager) {
        this.downLodRefId = downloadRefId;
        this.mContext = mContext;
        this.mListener = mListener;
        this.manager = manager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) != downLodRefId) {
                return;
            } else {
                listenDownloadStatus();
            }
        }
    }


    private void listenDownloadStatus() {

        new DownloadStatus(mContext, downLodRefId, manager, new DownloadStatus.DownloadStatusListener() {
            @Override
            public void OnDownloadListener(DownloadingState mState, String reason, Uri uri) {
                if (mState == DownloadingState.STATUS_FAILED) {

                    mListener.onDownloadError(reason);
                    Toast.makeText(mContext, reason, Toast.LENGTH_LONG).show();

                } else if (mState == DownloadingState.STATUS_PAUSED) {
                    Toast.makeText(mContext, reason, Toast.LENGTH_LONG).show();

                } else if (mState == DownloadingState.STATUS_PENDING) {
                    Toast.makeText(mContext, reason, Toast.LENGTH_LONG).show();

                } else if (mState == DownloadingState.STATUS_RUNNING) {
                    mListener.onDownloadRunning(true);
                    Toast.makeText(mContext, reason, Toast.LENGTH_LONG).show();

                } else if (mState == DownloadingState.STATUS_SUCCESSFUL) {
                    mListener.onDownloadCompleted(downLodRefId, reason, uri);

                } else {
                    mListener.onDownloadError(reason);
                    Toast.makeText(mContext, "cancelled", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
