package com.example.quynh.company.Objects;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.quynh.company.Constants.AppConstants;
import com.example.quynh.company.R;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Quynh on 12/9/2017.
 */

public class ProductionDetails implements Serializable {
    private String mName, mDescription;
    private Drawable mDrawable;
    private File mImageFile;
    private int mId;
    private long mPrice;

    public ProductionDetails(int id, String name, String description, File imageFile, long price) {
        mId = id;
        mName = name;
        mDescription = description;
        mImageFile = imageFile;
        mPrice = price;
    }

    public int getProductionId(){
        return mId;
    }

    public String getProductionName() {
        return mName;
    }

    public Drawable getProductionImage() {
        return mDrawable;
    }

    public File getProductionImageFile() {
        return mImageFile;
    }

    public String getProductionDescription() {
        return mDescription;
    }

    public long getProductionPrice() {
        return mPrice;
    }

}
