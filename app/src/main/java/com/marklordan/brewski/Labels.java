package com.marklordan.brewski;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mark on 01/07/2017.
 */

class Labels implements Serializable {
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("medium")
    private String mMediumLabel;
    @SerializedName("large")
    private String mLargeIcon;

    @SerializedName("squareMedium")
    private String mSquareMedium;

    @SerializedName("squareLarge")
    private String mSquareLarge;

    public String getmIcon() {
        return mIcon;
    }

    public String getmMediumLabel() {
        return mMediumLabel;
    }

    public String getmLargeIcon() {
        return mLargeIcon;
    }

    public String getmSquareMedium() {
        return mSquareMedium;
    }

    public String getmSquareLarge() {
        return mSquareLarge;
    }
}