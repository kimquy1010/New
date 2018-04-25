package com.example.quynh.company.Utils;

/**
 * Created by Quynh on 1/2/2018.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class CacheStore {
    private static final String TAG = "Cache Store";
    private static CacheStore INSTANCE = null;
    private HashMap<String, String> cacheMap;
    private HashMap<String, Bitmap> bitmapMap;
    private static final String cacheDir = "/android/data/cache/";
    private static final String CACHE_FILENAME = ".cache";


    private void cleanCacheStart() {
        cacheMap = new HashMap<String, String>();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
        fullCacheDir.mkdirs();
        File noMedia = new File(fullCacheDir.toString(), ".nomedia");
        try {
            noMedia.createNewFile();
            Log.i("CACHE", "Cache created");
        } catch (IOException e) {
            Log.i("CACHE", "Couldn't create .nomedia file");
            e.printStackTrace();
        }
    }

    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CacheStore();
        }
    }

    public static CacheStore getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

    public void saveCacheFileFromUrl(String name, String urlImage) {
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
        String fileLocalName = new SimpleDateFormat("ddMMyyhhmmssSSS").format(new java.util.Date()) + "_" + name + ".PNG";
        File fileUri = new File(fullCacheDir.toString(), fileLocalName);
        try {
            URL url = new URL(urlImage);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            FileOutputStream fileoutStream = new FileOutputStream(fileUri);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileoutStream.write(buffer, 0, bufferLength);
                downloadSize += bufferLength;
            }
            fileoutStream.flush();
            fileoutStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("CACHE", "Error: File could not be stuffed!");
            e.printStackTrace();
        }
    }

    public Bitmap getCacheFile(String cacheUri) {
        if (bitmapMap.containsKey(cacheUri)) return (Bitmap) bitmapMap.get(cacheUri);

        if (!cacheMap.containsKey(cacheUri)) return null;
        String fileLocalName = cacheMap.get(cacheUri).toString();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
        File fileUri = new File(fullCacheDir.toString(), fileLocalName);
        if (!fileUri.exists()) return null;

        Log.i("CACHE", "File " + cacheUri + " has been found in the Cache");
        Bitmap bm = BitmapFactory.decodeFile(fileUri.toString());
        bitmapMap.put(cacheUri, bm);
        return bm;
    }
}