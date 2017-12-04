package com.augugrumi.spacerace.pathCreator;

import android.support.annotation.NonNull;

import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.augugrumi.spacerace.utility.Costants.GOOGLE_MAPS_API_URL;

/**
 * Created by dpolonio on 16/11/17.
 */

class PathRetrieval {
    Call<Object> getDirections(@NonNull String origin, @NonNull String destination) {
        Retrofit request = new Retrofit.Builder()
                .baseUrl(GOOGLE_MAPS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MapsDirections directions = request.create(MapsDirections.class);
        return directions.getTravelInformation(
                origin,
                destination,
                "walking",
                "metric",
                SpaceRace.getAppContext().getString(R.string.google_maps_directions_api_key)
        );
    }
}
