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

    @SerializedName("labels")
    private BeerLabels mLabels;


    public Beer(String mTitle, String mDescription) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
    }

    public String getBeerTitle() {
        return mTitle;
    }

    public Brewery getBrewery(){
        return brewery[0];
    }

    public BeerLabels getBeerLabels() {
        return mLabels;
    }

    class BeerLabels{
        @SerializedName("icon")
        private String mIcon;
        @SerializedName("medium")
        private String mMediumLabel;
        @SerializedName("large")
        private String mLargeIcon;

        public String getmIcon() {
            return mIcon;
        }

        public String getmMediumLabel() {
            return mMediumLabel;
        }

        public String getmLargeIcon() {
            return mLargeIcon;
        }
    }
}
