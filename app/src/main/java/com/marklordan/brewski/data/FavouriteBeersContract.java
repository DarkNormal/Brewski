package com.marklordan.brewski.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mark on 02/07/2017.
 */

public class FavouriteBeersContract {


    public static final String AUTHORITY = "com.marklordan.Brewski";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_FAVOURITE_BEERS = "favouriteBeers";

    public static final class FavouriteBeersEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_BEERS).build();


        public static final String TABLE_NAME = "favouriteBeers";
        public static final String COLUMN_BEER_ID = "beerId";
        public static final String COLUMN_BEER_TITLE = "beerTitle";
    }
}
