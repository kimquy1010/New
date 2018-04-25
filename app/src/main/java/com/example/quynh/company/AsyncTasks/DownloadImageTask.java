package com.example.quynh.company.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Quynh on 1/2/2018.
 */

/**
 * AsyncTask download image from URL
 * result of this Task - send this image to Live stream activity
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private boolean isComplete;

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try {
            InputStream is = new URL(urlOfImage).openStream();
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            logo = BitmapFactory.decodeStream(is, null, o2);
            logo = Bitmap.createScaledBitmap(logo, 260, 260, false);
            isComplete = true;
        } catch (Exception e) { // Catch the download exception
            e.printStackTrace();
            isComplete = false;
        }
        return logo;
    }

    protected void onPostExecute(Bitmap result) {

    }
}

