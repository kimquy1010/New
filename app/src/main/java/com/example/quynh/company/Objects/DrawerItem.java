package com.example.quynh.company.Objects;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;

/**
 * Created by Quynh on 12/8/2017.
 */

public class DrawerItem {

    public static final int ITEM_HEADER = 0;
    public static final int ITEM_NORMAL = 1;
    public static final int ITEM_IMAGE = 2;
    private static final String TAG = "DrawableItem";
    private int mMainIconDrawable;
    private int mIconDrawableId = 0;
    private int mItemStatus;
    private String mHeaderTitle;
    private String mItemName;
    private Bitmap mIconBitmap = null;
    private File mIconFile = null;


    public DrawerItem(int imageMainId) {
        mMainIconDrawable = imageMainId;
        mItemStatus = ITEM_IMAGE;
    }

    public DrawerItem(int iconDrawableId, String itemName) {
        Log.d(TAG, "Using drawable item");
        mIconDrawableId = iconDrawableId;
        mItemName = itemName;
        mItemStatus = ITEM_NORMAL;
    }

    public DrawerItem(File iconFile, String itemName) {
        mIconFile = iconFile;
        mItemName = itemName;
        mItemStatus = ITEM_NORMAL;
    }

    public DrawerItem(Bitmap iconBitmap, String itemName) {
        mIconBitmap = iconBitmap;
        mItemName = itemName;
        mItemStatus = ITEM_NORMAL;
    }


    //All
    public String getItemName() {
        return mItemName;
    }

    public int getItemStatus() {
        return mItemStatus;
    }

    //For main
    public String getHeaderTitle() {
        return mHeaderTitle;
    }

    public int getMainImageId() {
        return mMainIconDrawable;
    }


    public int getIconDrawableId() {
        return mIconDrawableId;
    }

    public Bitmap getIconBitmap() {
        return mIconBitmap;
    }

    public File getIconFile() {
        return mIconFile;
    }
}