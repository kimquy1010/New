package com.example.quynh.company.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.quynh.company.Constants.AppConstants;
import com.example.quynh.company.R;

import java.io.File;

/**
 * Created by Quynh on 12/8/2017.
 */

public class CategoryDetails {
    private String mName;
    private File mImage;
    private int mId;

    public CategoryDetails(int id, String name, File image) {
        mId = id;
        mName = name;
        mImage = image;
    }

    public String getCategoryName() {
        return mName;
    }

    public File getCategoryImage() {
        return mImage;
    }

    public int getCategoryId() {
        return mId;
    }

}
