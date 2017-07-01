package com.marklordan.brewski;

import com.google.gson.annotations.SerializedName;

/**
 * Beer model class
 */

public class Beer {

    @SerializedName("name")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;

    @SerializedName("abv")
    private double mAbv;

    @SerializedName("breweries")
    private Brewery[] brewery;

    private String mBreweryName;

    public Beer(String mTitle, String mBreweryName) {
        this.mTitle = mTitle;
        this.mBreweryName = mBreweryName;
    }

    public Beer(String mTitle, String mDescription, String mBreweryName) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mBreweryName = mBreweryName;
    }

    public String getBeerTitle() {
        return mTitle;
    }

    public String getBreweryName() {
        return mBreweryName;
    }
}
