package com.example.quynh.company.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.ImageView;

import com.example.quynh.company.Constants.AppConstants;
import com.example.quynh.company.R;

import java.io.File;

/**
 * Created by Quynh on 12/9/2017.
 */

public class Utils {
    public static void gotoActivity(Context context, Class categorClass, int id) {
        Intent intent = new Intent(context, categorClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Integer.toString(id));
        context.startActivity(intent);
    }


    public static void sendMail(Activity activity, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:"
                + Uri.encode(AppConstants.SUPPORT_MAIL)
                + "?subject=" + Uri.encode(subject)
                + "&body=" + Uri.encode(body);
        Uri uri = Uri.parse(uriText);

        intent.setData(uri);
        activity.startActivity(Intent.createChooser(intent,
                activity.getString(R.string.choose_email_title)));
    }

    /**
     * @Method - Use to open facebook from this activity
     */
    public static void openFacebook(Context context, String pageId) {
        Intent intentFacebook;
        try {
            context.getPackageManager()
                    .getPackageInfo(AppConstants.LINK_TO_KATANA_FACEBOOK, 0); //Checks if FB is even installed.
            intentFacebook = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(AppConstants.LINK_TO_OPEN_PROFILE_KATANA + AppConstants.LINK_FACEBOOK)); //Try to make intent with FB's URI
//            intentFacebook.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentFacebook);
        } catch (Exception e) {
            intentFacebook = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(AppConstants.LINK_TO_FACEBOOK_BROWSER + pageId)); //catches and opens a url to the desired page
            intentFacebook.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentFacebook);
        }
    }

    public static void shareLink(Context context, String link) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(AppConstants.MIME_TEXT);
        shareIntent.putExtra(Intent.EXTRA_TEXT, link);
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_app_title)));
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void setImageViewFromFile(File file, ImageView imageView){
        if (file.exists()){
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }
    }
}
