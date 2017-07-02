package com.marklordan.brewski;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    public static final String BEER_KEY = "com.marklordan.brewski.DetailActivity.BEER_KEY";

    private Beer mCurrentBeer;
    private ImageView mBeerLabel, mBreweryBackground;
    private TextView mBeerTitle, mBeerDescription, mAbv, mIsOrganic;
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

        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mCurrentBeer.getBeerTitle());
        }catch(NullPointerException e){
            Log.e("DetailActivity", e.getMessage(), e);
        }

        //delays
        supportPostponeEnterTransition();

        if(mCurrentBeer != null){
            bindUi();
        }
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
}
