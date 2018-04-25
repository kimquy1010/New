package com.example.quynh.company.Helper;

import android.os.AsyncTask;

import com.example.quynh.company.Constants.AppConstants;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Quynh on 12/11/2017.
 */

public class CheckAppVersion extends AsyncTask<String,String,String>{
    String newVersion;
    @Override
    protected String doInBackground(String... strings) {
        try{
            newVersion = Jsoup.connect(AppConstants.APP_LINK + "&hl=en").timeout(30000).
                    userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
        } catch (IOException e){
            e.printStackTrace();
        }
        return newVersion;
    }
}
