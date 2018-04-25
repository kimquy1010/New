package com.example.quynh.company.Objects;

import java.io.File;

/**
 * Created by Quynh on 1/8/2018.
 */

public class MagazineDetails {
    private String mName;
    private File mImage;
    private int mId;
    private String mAddress;

    public MagazineDetails(int id, String name, String address, File image) {
        mId = id;
        mName = name;
        mAddress = address;
        mImage = image;
    }

    public int getMagazineId() {
        return mId;
    }

    public String getMagazineName() {
        return mName;
    }

    public File getMagazineImage() {
        return mImage;
    }

    public String getMagazineAddress() {
        return mAddress;
    }


}
