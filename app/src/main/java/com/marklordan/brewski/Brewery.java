package com.marklordan.brewski;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mark on 01/07/2017.
 */

public class Brewery implements Serializable{

    @SerializedName("name")
    public String mName;

    @SerializedName("description")
    public String mDescription;

    @SerializedName("established")
    public int mYearEstablished;

    @SerializedName("website")
    public String mWebsite;

    @SerializedName("status")
    public String mIsVerified;

    @SerializedName("images")
    public Labels mBreweryImages;

    public String getBreweryName() {
        return mName;
    }

    public Labels getmBreweryImages() {
        return mBreweryImages;
    }
}
