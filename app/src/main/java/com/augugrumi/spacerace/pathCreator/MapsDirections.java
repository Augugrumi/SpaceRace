package com.augugrumi.spacerace.pathCreator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dpolonio on 16/11/17.
 */

interface MapsDirections {

    @GET("maps/api/directions/json")
    Call<Object> getTravelInformation(@Query("origin") String origin,
                                      @Query("destination") String dest,
                                      @Query("mode") String mode,
                                      @Query("units") String unit,
                                      @Query("key") String apiKey);
}