package com.marklordan.brewski;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BeerAdapter.BeerItemClickListener{

    public static final String BEER_LIST = "com.marklordan.brewski.BEER_LIST";
    public static final String RECYCLERVIEW_LAYOUT = "com.marklordan.brewski.LAYOUT";

    private ArrayList<Beer> beerList = new ArrayList<>();
    BreweryService service;
    private BeerAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isListSelected = true;

    private Retrofit retrofit;
    private Toolbar toolbar;
    private ProgressBar mProgressBar;
    private LinearLayout mNetworkErrorView;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            beerList = (ArrayList<Beer>) savedInstanceState.getSerializable(BEER_LIST);
            isListSelected = savedInstanceState.getBoolean(RECYCLERVIEW_LAYOUT);
        }
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), calculateNoOfColumns(getApplicationContext()));
        //Recyclerview Setup
        recyclerView = (RecyclerView) findViewById(R.id.beer_recyclerview);
        if(isListSelected){

            adapter = new BeerAdapter(beerList, this, this, true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);

            //Recyclerview divivders between items
            dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        }else{

            adapter = new BeerAdapter(beerList, this, this, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBeers();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(BreweryService.class);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //These are for displaying the progressbar on load, or the error message on fail
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_indicator);
        mNetworkErrorView = (LinearLayout) findViewById(R.id.network_error_view);

        //if there are no beers from savedInstanceState, get them from the API
        if(beerList.size() == 0){
            refreshBeers();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        String layoutName = (isListSelected ? getString(R.string.list_options) : getString(R.string.grid_options));
        MenuItem layoutOption = menu.findItem(R.id.view_option);
        layoutOption.setTitle(layoutName);
        layoutOption.setIcon((isListSelected ? getDrawable(R.drawable.ic_format_list_bulleted) : getDrawable(R.drawable.ic_grid_on)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isListSelected){
            item.setIcon(R.drawable.ic_grid_on);
            item.setTitle(getString(R.string.grid_options));
            isListSelected = false;
            adapter = new BeerAdapter(beerList, this, this, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.removeItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(adapter);
        } else{
            item.setIcon(R.drawable.ic_format_list_bulleted);
            item.setTitle(getString(R.string.list_options));
            adapter = new BeerAdapter(beerList, this, this, true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            isListSelected = true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the beers to the bundle for use on rotation
        outState.putSerializable(BEER_LIST, beerList);
        outState.putBoolean(RECYCLERVIEW_LAYOUT, isListSelected);
    }

    //Callback for launching DetailsActivity when an item is clicked
    @Override
    public void onItemClick(int itemClicked, ImageView beerImage, TextView beerTitle) {
        Intent intent = DetailActivity.newInstance(this, beerList.get(itemClicked));

        //Shared Transition pairs between both activities
        Pair<View, String> p1 = Pair.create((View)toolbar, ViewCompat.getTransitionName(toolbar));
        Pair<View, String> p2 = Pair.create((View) beerImage, ViewCompat.getTransitionName(beerImage));
        Pair<View, String> p3 = Pair.create((View) beerTitle, ViewCompat.getTransitionName(beerTitle));


        //Shared Element Transition
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p3);
        startActivity(intent, options.toBundle());
    }

    private void refreshBeers(){
        getBeers(new BeerCallback() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
                //switch out the progressbar for the recyclerview
                mProgressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError() {
                //switch out the progressbar for the error view
                mProgressBar.setVisibility(View.INVISIBLE);
                mNetworkErrorView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }


    //Callback interface for use with Retrofit
    public interface BeerCallback{
        void onSuccess();
        void onError();
    }

    public void getBeers(final BeerCallback callback){
        Call<JsonObject> beers = service.getBeers(BuildConfig.API_KEY);
        if(!mSwipeRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mNetworkErrorView.setVisibility(View.INVISIBLE);

        beers.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray data = response.body().getAsJsonArray("data");
                beerList.clear();
                //Parse the JSON to POJOs and store in a list
                for (JsonElement element : data ) {
                    Beer beer = new Gson().fromJson(element, Beer.class);
                    beerList.add(beer);
                }
                callback.onSuccess();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("MainActivity", "Error in getBeers", t);
                callback.onError();
            }
        });
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
