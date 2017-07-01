package com.marklordan.brewski;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Beer model class
 */

public class Beer implements Serializable {

    @SerializedName("name")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;

    @SerializedName("abv")
    private double mAbv;

    @SerializedName("breweries")
    private Brewery[] brewery;

    @SerializedName("labels")
    private Labels mLabels;


    public Beer(String mTitle, String mDescription) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
    }

    public String getBeerTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public double getmAbv() {
        return mAbv;
    }

    public Brewery getBrewery(){
        return brewery[0];
    }

    public Labels getBeerLabels() {
        return mLabels;
    }
}
