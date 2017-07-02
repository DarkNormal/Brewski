package com.marklordan.brewski.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mark on 02/07/2017.
 */

public class FavouriteBeersDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouriteBeers.db";
    private static final int DATABASE_VERSION = 1;

    public FavouriteBeersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITE_BEERS_TABLE = "CREATE TABLE " + FavouriteBeersContract.FavouriteBeersEntry.TABLE_NAME + " (" +
                FavouriteBeersContract.FavouriteBeersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouriteBeersContract.FavouriteBeersEntry.COLUMN_BEER_ID + " TEXT NOT NULL, " +
                FavouriteBeersContract.FavouriteBeersEntry.COLUMN_BEER_TITLE + " TEXT NOT NULL" +
                "); ";
        db.execSQL(SQL_CREATE_FAVOURITE_BEERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS");

    }
}
