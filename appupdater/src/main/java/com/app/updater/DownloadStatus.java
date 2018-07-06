package com.app.updater;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Pravesh Sharma on 06-07-2018.
 */

class DownloadStatus {

    private static final String TAG = DownloadStatus.class.getSimpleName();
    private final DownloadManager mDownloadManager;
    private final Context mContext;
    private long downloadReferenceId;
    private DownloadingState mDownloadingState;
    private DownloadStatusListener mStatusListener;
    private Uri uri=null;


    DownloadStatus(Context mContext, long downloadReferenceId, DownloadManager mDownloadManager, DownloadStatusListener mStatusListener) {
        this.downloadReferenceId = downloadReferenceId;
        this.mDownloadManager = mDownloadManager;
        this.mContext = mContext;
        this.mStatusListener = mStatusListener;
        getDownloadStatus();
    }

    private void getDownloadStatus() {


        DownloadManager.Query mDownloadQuery = new DownloadManager.Query();
        if (downloadReferenceId != 0) {
            mDownloadQuery.setFilterById(downloadReferenceId);

            if (mDownloadManager != null) {
                Cursor cursor = mDownloadManager.query(mDownloadQuery);
                if (cursor != null && cursor.moveToFirst()) {
                    checkStatus(cursor, downloadReferenceId);
                } else {

                }
            }
        }

//        listenForDownloadCancel();
    }


    private void listenForDownloadCancel() {

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName(), file);
        mContext.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"),
                true, new ContentObserver(null) {
                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        super.onChange(selfChange, uri);
                        if (uri.toString().matches(".*\\d+$")) {
                            long changedId = Long.parseLong(uri.getLastPathSegment());
                            if (changedId == downloadReferenceId) {
                                Log.d(TAG, "onChange: " + uri.toString() + " " + changedId + " " + downloadReferenceId);
                                Toast.makeText(mContext, "onchange", Toast.LENGTH_LONG).show();
                                Cursor cursor = null;
                                try {
                                    cursor = mContext.getContentResolver().query(uri, null, null, null, null);
                                    if (cursor != null && cursor.moveToFirst()) {
                                        Log.d(TAG, "onChange: running");
                                        Toast.makeText(mContext, "onchange", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.w(TAG, "onChange: cancel");
                                        Toast.makeText(mContext, "onchange", Toast.LENGTH_LONG).show();
                                    }
                                } finally {
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                }
                            }
                        }

                    }
                });
    }

    private void checkStatus(Cursor cursor, long downloadReferenceId) {
        //column for download  status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        int filenameIndexUri = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
        String fileUri = cursor.getString(filenameIndexUri);
        String filename = null;
        if (fileUri != null) {
            File mFile = new File(Uri.parse(fileUri).getPath());
            filename = mFile.getAbsolutePath();
        }

        String reasonText = "";

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                mDownloadingState = DownloadingState.STATUS_FAILED;
                switch (reason) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = mContext.getString(R.string.ERROR_CANNOT_RESUME);
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = mContext.getString(R.string.ERROR_DEVICE_NOT_FOUND);
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = mContext.getString(R.string.ERROR_FILE_ALREADY_EXISTS);
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = mContext.getString(R.string.ERROR_FILE_ERROR);
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = mContext.getString(R.string.ERROR_HTTP_DATA_ERROR);
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = mContext.getString(R.string.ERROR_INSUFFICIENT_SPACE);
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = mContext.getString(R.string.ERROR_TOO_MANY_REDIRECTS);
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = mContext.getString(R.string.ERROR_HTTP_DATA_ERROR);
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                mDownloadingState = DownloadingState.STATUS_PAUSED;
                switch (reason) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = mContext.getString(R.string.PAUSED_QUEUED_FOR_WIFI);
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = mContext.getString(R.string.PAUSED_UNKNOWN);
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = mContext.getString(R.string.PAUSED_WAITING_FOR_NETWORK);
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = mContext.getString(R.string.PAUSED_WAITING_TO_RETRY);
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                mDownloadingState = DownloadingState.STATUS_PENDING;
                reasonText = mContext.getString(R.string.STATUS_PENDING);
                break;
            case DownloadManager.STATUS_RUNNING:
                mDownloadingState = DownloadingState.STATUS_RUNNING;
                reasonText = mContext.getString(R.string.STATUS_RUNNING);
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                mDownloadingState = DownloadingState.STATUS_SUCCESSFUL;
                reasonText = mContext.getString(R.string.STATUS_SUCCESSFUL) + "Filename:\n" + filename;
                String uriString = cursor
                        .getString(cursor
                                .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                 uri = Uri.parse(uriString);
                break;

            default:

                break;
        }

        if (mStatusListener != null)
            mStatusListener.OnDownloadListener(mDownloadingState, reasonText,uri);

    }


    interface DownloadStatusListener {
        void OnDownloadListener(DownloadingState mState, String reason,Uri uri);
    }

}