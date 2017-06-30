package com.marklordan.brewski;

/**
 * Beer model class
 */

public class Beer {

    private String mTitle, mBreweryName;

    public Beer(String beerName, String beerBrewery){
        mTitle = beerName;
        mBreweryName = beerBrewery;
    }

    public String getBeerTitle() {
        return mTitle;
    }

    public String getBreweryName() {
        return mBreweryName;
    }
}
