package com.marklordan.brewski;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mark on 01/07/2017.
 */

public class Brewery {

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

    public String getBreweryName() {
        return mName;
    }
}
