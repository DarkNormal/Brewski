package com.marklordan.brewski;

import com.google.gson.JsonObject;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mark on 30/06/2017.
 */

public interface BreweryService {

    @GET("beers/?name=****&withBreweries=Y&&hasLabels=Y")
    Call<JsonObject> getBeers(@Query("key") String apiKey);
}
