package com.marklordan.brewski.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.marklordan.brewski.data.FavouriteBeersContract.FavouriteBeersEntry.TABLE_NAME;


public class FavouriteContentProvider extends ContentProvider {

    private FavouriteBeersDbHelper mDbHelper;

    public static final int FAVOURITE_BEERS = 100;
    public static final int FAVOURITE_BEERS_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavouriteBeersContract.AUTHORITY, FavouriteBeersContract.PATH_FAVOURITE_BEERS, FAVOURITE_BEERS);
        uriMatcher.addURI(FavouriteBeersContract.AUTHORITY, FavouriteBeersContract.PATH_FAVOURITE_BEERS + "/*", FAVOURITE_BEERS_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new FavouriteBeersDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FAVOURITE_BEERS:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case FAVOURITE_BEERS:
                // Inserting values into tasks table
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavouriteBeersContract.FavouriteBeersEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int favouritesRemoved;

        switch (match){
            case FAVOURITE_BEERS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                favouritesRemoved = db.delete(TABLE_NAME,
                        FavouriteBeersContract.FavouriteBeersEntry.COLUMN_BEER_ID + " =?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);

        }
        if(favouritesRemoved != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favouritesRemoved;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
