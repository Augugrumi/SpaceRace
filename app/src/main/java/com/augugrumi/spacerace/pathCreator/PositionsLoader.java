package com.augugrumi.spacerace.pathCreator;

import android.content.res.Resources;

import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davide on 17/11/17.
 */

class PositionsLoader {

    private static final int[] POSITIONS = {
            R.array.basilica_sant_antonio,
            R.array.battistero_duomo,
            R.array.bo,
            R.array.borgo_altinate,
            R.array.caffe_pedrocchi,
            R.array.cappella_degli_scrovegni,
            R.array.casa_galileo,
            R.array.chiesa_degli_eremitani,
            R.array.chiesa_san_francesco,
            R.array.chiesa_san_nicolo,
            R.array.chiesa_san_pietro,
            R.array.chiesa_san_tommaso,
            R.array.chiesa_santa_sofia,
            R.array.oratorio_san_rocco,
            R.array.orologio_civile,
            R.array.palazzo_della_ragione,
            R.array.piazza_capitaniato,
            R.array.piazza_dei_signori,
            R.array.prato_valle
    };

    static List<LatLng> getPositions() {

        Resources r = SpaceRace.getAppContext().getResources();

        List<LatLng> buildingsPositions = new ArrayList<>();

        for (int place : POSITIONS) {
            buildingsPositions.add(new LatLng(
                    Double.parseDouble(r.getStringArray(place)[0]),
                    Double.parseDouble(r.getStringArray(place)[1])
            ));
        }

        return buildingsPositions;
    }
}
