package com.marklordan.brewski;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.marklordan.brewski.data.FavouriteBeersContract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    public static final String BEER_KEY = "com.marklordan.brewski.DetailActivity.BEER_KEY";

    private Beer mCurrentBeer;
    private ImageView mBeerLabel, mBreweryBackground;
    private TextView mBeerTitle, mBeerDescription, mAbv, mIsOrganic;
    private LikeButton mLikeButton;
    private DecimalFormat df = new DecimalFormat("#.#");

    public static Intent newInstance(Context context, Beer beer) {
        Intent detailsIntent = new Intent(context, DetailActivity.class);
        detailsIntent.putExtra(BEER_KEY, beer);
        return detailsIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        try{
            mCurrentBeer = (Beer) bundle.getSerializable(BEER_KEY);
        } catch(ClassCastException e){
            Log.e(DetailActivity.class.toString(), e.getMessage(), e);
        }
        mBeerLabel = (ImageView) findViewById(R.id.beerLabelImageView);
        mBreweryBackground = (ImageView) findViewById(R.id.beerBackgroundImageView);
        mBeerTitle = (TextView) findViewById(R.id.beerTitleTextView);
        mBeerDescription = (TextView) findViewById(R.id.beerDescriptionTextView);
        mAbv = (TextView) findViewById(R.id.beerAbvTextView);
        mIsOrganic = (TextView) findViewById(R.id.beerIsOrganicTextView);
        mLikeButton = (LikeButton) findViewById(R.id.favourite_button);


        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mCurrentBeer.getBeerTitle());
        }catch(NullPointerException e){
            Log.e("DetailActivity", e.getMessage(), e);
        }

        supportPostponeEnterTransition();

        if(mCurrentBeer != null){
            bindUi();
        }

        Cursor cursor = getContentResolver().query(FavouriteBeersContract.FavouriteBeersEntry.CONTENT_URI,
                null,
                FavouriteBeersContract.FavouriteBeersEntry.COLUMN_BEER_ID + " = '" + mCurrentBeer.getmId() + "'",
                null,
                null,
                null);

        if (cursor != null) {
            while(cursor.moveToNext()){
                mLikeButton.setLiked(true);
            }
        }
        cursor.close();

        mLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ContentValues cv = buildBeerContentValues();
                addBeerToFavouriteDb(cv);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                removeBeerFromFavouriteDb();
            }
        });
    }

    private void bindUi(){
        Picasso.with(this).load(mCurrentBeer.getBeerLabels().getmMediumLabel())
                .noFade()
                .into(mBeerLabel, new Callback() {
            @Override
            public void onSuccess() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onError() {
                supportStartPostponedEnterTransition();
            }
        });
        mBeerTitle.setText(mCurrentBeer.getBeerTitle());
        mBeerDescription.setText(mCurrentBeer.getmDescription());
        mAbv.setText(getString(R.string.abv, df.format(mCurrentBeer.getmAbv())));
        String backgroundUrl = (mCurrentBeer.getBrewery().getmBreweryImages() == null ? mCurrentBeer.getBeerLabels().getmLargeIcon() :
                mCurrentBeer.getBrewery().getmBreweryImages().getmLargeIcon());
        Picasso.with(this).load(backgroundUrl).into(mBreweryBackground);
        String isOrganicBeer = getString(R.string.organic, mCurrentBeer.getIsOrganic());
        mIsOrganic.setText(isOrganicBeer);
    }

    private ContentValues buildBeerContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(FavouriteBeersContract.FavouriteBeersEntry.COLUMN_BEER_ID, mCurrentBeer.getmId());
        cv.put(FavouriteBeersContract.FavouriteBeersEntry.COLUMN_BEER_TITLE, mCurrentBeer.getBeerTitle());
        return cv;
    }

    private void addBeerToFavouriteDb(ContentValues cv){
        getContentResolver().insert(FavouriteBeersContract.FavouriteBeersEntry.CONTENT_URI, cv);
    }

    private void removeBeerFromFavouriteDb(){
        String id = mCurrentBeer.getmId();
        Uri uri = FavouriteBeersContract.FavouriteBeersEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();

        getContentResolver().delete(uri, null,null);

    }
}
