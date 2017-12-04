/**
* Copyright 2017 Davide Polonio <poloniodavide@gmail.com>, Federico Tavella
* <fede.fox16@gmail.com> and Marco Zanella <zanna0150@gmail.com>
* 
* This file is part of SpaceRace.
* 
* SpaceRace is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SpaceRace is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with SpaceRace.  If not, see <http://www.gnu.org/licenses/>.
*/

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
