package com.apk.downloader;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.updater.AppUpdater;


public class MainActivity extends AppCompatActivity {

    private AppUpdater appUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "http://www.appsapk.com/downloading/latest/Sound%20Profile%20(+%20volume%20scheduler)-5.25.apk";
        appUpdater = new AppUpdater(this, url.trim());
        appUpdater.downloadApk();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        appUpdater.unregisterBroadcast();
    }
}


//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private DownloadManager downloadManager;
//
//    private long Image_DownloadId, Music_DownloadId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.content_main);
//
//        //Download Image from URL
//        Button DownloadImage = (Button) findViewById(R.id.DownloadImage);
//        DownloadImage.setOnClickListener(this);
//
//        //Download Music from URL
//        Button DownloadMusic = (Button) findViewById(R.id.DownloadMusic);
//        DownloadMusic.setOnClickListener(this);
//
//        //Check Download status
//        Button DownloadStatus = (Button) findViewById(R.id.DownloadStatus);
//        DownloadStatus.setOnClickListener(this);
//        DownloadStatus.setEnabled(false);
//
//        //Cancel Current Download
//        Button CancelDownload = (Button) findViewById(R.id.CancelDownload);
//        CancelDownload.setOnClickListener(this);
//        CancelDownload.setEnabled(false);
//
//        //set filter to only when download is complete and register broadcast receiver
//        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        registerReceiver(downloadReceiver, filter);
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//
//            //Download Image
//            case R.id.DownloadImage:
//                Uri image_uri = Uri.parse("http://www.androidtutorialpoint.com/wp-content/uploads/2016/09/Beauty.jpg");
//                Image_DownloadId = DownloadData(image_uri, v);
//                break;
//
//            //Download Music
//            case R.id.DownloadMusic:
//                Uri music_uri = Uri.parse("http://www.androidtutorialpoint.com/wp-content/uploads/2016/09/AndroidDownloadManager.mp3");
//                Music_DownloadId = DownloadData(music_uri, v);
//                break;
//
//            //check the status of all downloads
//            case R.id.DownloadStatus:
//
//                Check_Image_Status(Image_DownloadId);
//                Check_Music_Status(Music_DownloadId);
//
//                break;
//
//            //cancel the ongoing download
//            case R.id.CancelDownload:
//
//                downloadManager.remove(Image_DownloadId);
//                downloadManager.remove(Music_DownloadId);
//                break;
//
//        }
//    }
//
//    private void Check_Image_Status(long Image_DownloadId) {
//
//        DownloadManager.Query ImageDownloadQuery = new DownloadManager.Query();
//        //set the query filter to our previously Enqueued download
//        ImageDownloadQuery.setFilterById(Image_DownloadId);
//
//        //Query the download manager about downloads that have been requested.
//        Cursor cursor = downloadManager.query(ImageDownloadQuery);
//        if(cursor.moveToFirst()){
//            DownloadStatus(cursor, Image_DownloadId);
//        }
//
//    }
//
//    private void Check_Music_Status(long Music_DownloadId) {
//
//        DownloadManager.Query MusicDownloadQuery = new DownloadManager.Query();
//        //set the query filter to our previously Enqueued download
//        MusicDownloadQuery.setFilterById(Music_DownloadId);
//
//        //Query the download manager about downloads that have been requested.
//        Cursor cursor = downloadManager.query(MusicDownloadQuery);
//        if(cursor.moveToFirst()){
//            DownloadStatus(cursor, Music_DownloadId);
//        }
//
//    }
//
//    private void DownloadStatus(Cursor cursor, long DownloadId){
//
//        //column for download  status
//        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
//        int status = cursor.getInt(columnIndex);
//        //column for reason code if the download failed or paused
//        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
//        int reason = cursor.getInt(columnReason);
//        //get the download filename
//        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
//        String filename = cursor.getString(filenameIndex);
//
//        String statusText = "";
//        String reasonText = "";
//
//        switch(status){
//            case DownloadManager.STATUS_FAILED:
//                statusText = "STATUS_FAILED";
//                switch(reason){
//                    case DownloadManager.ERROR_CANNOT_RESUME:
//                        reasonText = "ERROR_CANNOT_RESUME";
//                        break;
//                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
//                        reasonText = "ERROR_DEVICE_NOT_FOUND";
//                        break;
//                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
//                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
//                        break;
//                    case DownloadManager.ERROR_FILE_ERROR:
//                        reasonText = "ERROR_FILE_ERROR";
//                        break;
//                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
//                        reasonText = "ERROR_HTTP_DATA_ERROR";
//                        break;
//                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
//                        reasonText = "ERROR_INSUFFICIENT_SPACE";
//                        break;
//                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
//                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
//                        break;
//                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
//                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
//                        break;
//                    case DownloadManager.ERROR_UNKNOWN:
//                        reasonText = "ERROR_UNKNOWN";
//                        break;
//                }
//                break;
//            case DownloadManager.STATUS_PAUSED:
//                statusText = "STATUS_PAUSED";
//                switch(reason){
//                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
//                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
//                        break;
//                    case DownloadManager.PAUSED_UNKNOWN:
//                        reasonText = "PAUSED_UNKNOWN";
//                        break;
//                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
//                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
//                        break;
//                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
//                        reasonText = "PAUSED_WAITING_TO_RETRY";
//                        break;
//                }
//                break;
//            case DownloadManager.STATUS_PENDING:
//                statusText = "STATUS_PENDING";
//                break;
//            case DownloadManager.STATUS_RUNNING:
//                statusText = "STATUS_RUNNING";
//                break;
//            case DownloadManager.STATUS_SUCCESSFUL:
//                statusText = "STATUS_SUCCESSFUL";
//                reasonText = "Filename:\n" + filename;
//                break;
//        }
//
//        if(DownloadId == Music_DownloadId) {
//
//            Toast toast = Toast.makeText(MainActivity.this,
//                    "Music Download Status:" + "\n" + statusText + "\n" +
//                            reasonText,
//                    Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.TOP, 25, 400);
//            toast.show();
//
//        }
//        else {
//
//            Toast toast = Toast.makeText(MainActivity.this,
//                    "Image Download Status:"+ "\n" + statusText + "\n" +
//                            reasonText,
//                    Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.TOP, 25, 400);
//            toast.show();
//
//            // Make a delay of 3 seconds so that next toast (Music Status) will not merge with this one.
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }, 3000);
//
//        }
//
//    }
//
//    private long DownloadData (Uri uri, View v) {
//
//        long downloadReference;
//
//        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//
//        //Setting title of request
//        request.setTitle("Data Download");
//
//        //Setting description of request
//        request.setDescription("Android Data download using DownloadManager.");
//
//        //Set the local destination for the downloaded file to a path within the application's external files directory
//        if(v.getId() == R.id.DownloadMusic)
//            request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS,"AndroidTutorialPoint.mp3");
//        else if(v.getId() == R.id.DownloadImage)
//            request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS,"AndroidTutorialPoint.jpg");
//
//        //Enqueue download and save the referenceId
//        downloadReference = downloadManager.enqueue(request);
//
//        Button DownloadStatus = (Button) findViewById(R.id.DownloadStatus);
//        DownloadStatus.setEnabled(true);
//        Button CancelDownload = (Button) findViewById(R.id.CancelDownload);
//        CancelDownload.setEnabled(true);
//
//        return downloadReference;
//    }
//
//    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            //check if the broadcast message is for our Enqueued download
//            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//
//            if(referenceId == Image_DownloadId) {
//
//                Toast toast = Toast.makeText(MainActivity.this,
//                        "Image Download Complete", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.TOP, 25, 400);
//                toast.show();
//            }
//            else if(referenceId == Music_DownloadId) {
//
//                Toast toast = Toast.makeText(MainActivity.this,
//                        "Music Download Complete", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.TOP, 25, 400);
//                toast.show();
//            }
//
//        }
//    };
//
//}
