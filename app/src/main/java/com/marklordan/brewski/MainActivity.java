package com.marklordan.brewski;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

    private ArrayList<Beer> beerList = new ArrayList<>();
    BreweryService service;
    private BeerAdapter adapter;
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recyclerview Setup
        recyclerView = (RecyclerView) findViewById(R.id.beer_recyclerview);
        adapter = new BeerAdapter(beerList, this, this);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        //Recyclerview divivders between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(BreweryService.class);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        getBeers(new BeerCallback() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
            }
        });
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


    //Callback interface for use with Retrofit
    public interface BeerCallback{
        void onSuccess();
    }

    public void getBeers(final BeerCallback callback){
        Call<JsonObject> beers = service.getBeers(BuildConfig.API_KEY);

        beers.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray data = response.body().getAsJsonArray("data");

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
            }
        });
    }
}
