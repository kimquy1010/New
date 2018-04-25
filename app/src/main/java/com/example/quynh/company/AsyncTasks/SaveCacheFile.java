package com.example.quynh.company.AsyncTasks;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by Quynh on 1/3/2018.
 */

public class SaveCacheFile extends AsyncTask<String, Void, File> {
    private static final String TAG = "SaveCachFile";
    private static final String CACHE_DIR = "/android/data/cache/";
    private static final long INTERVAL_DAY = TimeUnit.DAYS.toMillis(1);
    private int mId;
    private File fileUri;
    private String mTypeOfFile;


    public SaveCacheFile(String typeOfFile, int id) {
        mTypeOfFile = typeOfFile;
        mId = id;
    }

    @Override
    protected File doInBackground(String... urls) {
        try {
            Log.d(TAG, "Save image to cache");
            File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), CACHE_DIR);
            if (!fullCacheDir.exists()) {
                try {
                    fullCacheDir.mkdirs();
                    Log.d(TAG, "Created directory");
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
            String fileLocalName = mTypeOfFile + "_" + mId + ".png";
            Log.d(TAG, "Test file name: " + fileLocalName);
            fileUri = new File(fullCacheDir.toString(), fileLocalName);
            Log.d(TAG, "Test file uri: " + fileUri.toString());
            if (!fileUri.exists()) {
                try {
                    fileUri.createNewFile();
                    Log.d(TAG, "Created file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "Test time last modified: " + fileUri.lastModified() + ", " + System.currentTimeMillis());

                if (System.currentTimeMillis() - fileUri.lastModified() < INTERVAL_DAY) {
                    return fileUri;
                }
            }

            Log.d(TAG, "Start downloading from server");

            URL imageUrl = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) imageUrl.openConnection();
            urlConnection.connect();
            FileOutputStream fileOutStream = new FileOutputStream(fileUri);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            Log.d(TAG, "Test file from internet length: " + totalSize);
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutStream.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i(TAG, "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
            }
            fileOutStream.flush();
            fileOutStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("CACHE", "Error: File could not be stuffed!");
            e.printStackTrace();
        }
        return fileUri;
    }
}
