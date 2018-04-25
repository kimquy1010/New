package com.example.quynh.company.Constants;

import android.os.Environment;

import com.example.quynh.company.BuildConfig;
import com.example.quynh.company.R;

import java.io.File;

/**
 * Created by Quynh on 12/8/2017.
 */

public class AppConstants {
    public static final String PERFUME_IMAGE_LINK = "https://fimgs.net/images/perfume/375x500.609.jpg";
    public static final String COMPANY_NAME = "QUYNH";

    public static final String PERFUME = "PERFUME";
    public static final String PERFUME_1 = "PERFUME 1";
    public static final String PERFUME_2 = "PERFUME 2";
    public static final String PERFUME_3 = "PERFUME 3";


    public static final String IPHONE = "IPHONE";
    public static final String IPHONE_6 = "IPHONE 6";
    public static final String IPHONE_7 = "IPHONE 7";
    public static final String IPHONE_8 = "IPHONE 8";
    public static final String IPHONE_IMAGE_LINK = "https://i5.walmartimages.com/asr/067883b4-2301-41b4-afc5-665d12c3e121_1.f583d9ee5b2fc78a0dd64a305adf3ef4.jpeg";
    public static final String IPHONE_6_IMAGE_LINK = "https://i5.walmartimages.com/asr/067883b4-2301-41b4-afc5-665d12c3e121_1.f583d9ee5b2fc78a0dd64a305adf3ef4.jpeg";
    public static final String IPHONE_7_IMAGE_LINK = "https://i5.walmartimages.com/asr/067883b4-2301-41b4-afc5-665d12c3e121_1.f583d9ee5b2fc78a0dd64a305adf3ef4.jpeg";
    public static final String IPHONE_8_IMAGE_LINK = "https://i5.walmartimages.com/asr/067883b4-2301-41b4-afc5-665d12c3e121_1.f583d9ee5b2fc78a0dd64a305adf3ef4.jpeg";

    //Share and feedback
    public static final String LINK_TO_KATANA_FACEBOOK = "com.facebook.katana";
    public static final String LINK_TO_OPEN_PAGE_KATANA = "fb://page/";
    public static final String LINK_TO_OPEN_PROFILE_KATANA = "fb://facewebmodal/f?href=";
    public static final String LINK_TO_FACEBOOK_BROWSER = "https://www.facebook.com/";
    public static final String PAGE_ID = "507252059400308";//"quynh.ho.904";//
    public static final String LINK_FACEBOOK = LINK_TO_FACEBOOK_BROWSER + PAGE_ID;
    public static final String MIME_TEXT = "text/plain";
    public static final String APP_PACKAGE_NAME = ""; //App name
    public static final String APP_MARKET_ADDRESS = "market://details?id=" + APP_PACKAGE_NAME;
    public static final String APP_LINK = "https://play.google.com/store/apps/details?id=" + APP_PACKAGE_NAME;
    public static final String SUPPORT_MAIL = "kimquy281091@gmail.com";
    public static final String EXTRA_MAIL_SUBJECT_REPORT = "Report bug and suggestion for version "
            + BuildConfig.VERSION_NAME;
    public static final String EXTRA_MAIL_MSG_REPORT = "Hello " + COMPANY_NAME + "!";

    //Language
    public static final String DEVICE_LANGUAGE = "device";
    public static final String KEY_EXTRA_LANGUAGE = "lang";
    public static final int REQUEST_CODE_SELECT_LANGUAGE = 1216;

    //Review
    public static final String REVIEW_PRODUCTION = "review_production";
    public static final String REVIEW_PRODUCTION_NAME = "review_production_name";
    public static final String REVIEW_PRODUCTION_IMAGE = "review_production_image";
    public static final String REVIEW_PRODUCTION_DESCRIPTION = "review_production_description";

    //Link to get data
    public static final String LOCAL_HOST = "192.168.0.113";
    public static final String LINK_CATEGORY = "http://" + LOCAL_HOST + "/server/getCategory.php";
    public static final String LINK_PRODUCTION = "http://" + LOCAL_HOST + "/server/getProductionByCategory.php";
    public static final String LINK_NEW_PRODUCTION = "http://" + LOCAL_HOST + "/server/getNewProduction.php";
    public static final String LINK_MAGAZINE_ADDRESS = "http://" + LOCAL_HOST + "/server/getMagazineByProduction.php";

    //Type of file
    public static final String FILE_CATEGORY = "category";
    public static final String FILE_NEW_PRODUCTION = "new_production";
    public static final String FILE_PRODUCTION = "production";
    private static final String CACHE_DIR = "/android/data/cache/";
    public static final String FULL_CACHE_DIR = Environment.getExternalStorageDirectory().toString() + CACHE_DIR;

    //Animation flag

    public static final int FLAG_RESOLUTION = 32;

    public static final int FLAG_TEXTURE = R.drawable.ic_flag_new_production;

    public static final float FLAG_WAVE_LENGTH = 5.0f;

    public static final int FLAG_SKIP_FRAMES = 1;

    public static final float FLAG_ROTATION_X = -22.5f;

    public static final float FLAG_ROTATION_Y = 0.0f;

    public static final float FLAG_ROTATION_Z = 0.0f;

}
