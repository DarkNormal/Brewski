package com.marklordan.brewski;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private ArrayList<Beer> beerList = new ArrayList<>();
    BreweryService service;
    private BeerAdapter adapter;
    private RecyclerView recyclerView;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.beer_recyclerview);
        adapter = new BeerAdapter(beerList, this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(BreweryService.class);
        getBeers(new BeerCallback() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
    }


    public interface BeerCallback{
        void onSuccess();
        void onError();
    }

    public void getBeers(final BeerCallback callback){
        Call<JsonObject> beers = service.getBeers(BuildConfig.API_KEY);

        beers.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(MainActivity.this, "succesful response", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", response.toString());
                JsonArray data = response.body().getAsJsonArray("data");
                for (JsonElement element : data ) {
                    Beer beer = new Gson().fromJson(element, Beer.class);
                    beerList.add(beer);
                }
                callback.onSuccess();

                for (Beer b : beerList) {
                    System.out.println(b.getBeerTitle());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(MainActivity.this, "unsuccessful response", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
