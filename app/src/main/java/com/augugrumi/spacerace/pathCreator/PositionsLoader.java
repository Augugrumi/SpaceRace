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
            R.array.position_2_sant_antonio_basilica,
            R.array.position_17_battistero_duomo,
            R.array.position_11_galileo_university,
            R.array.position_6_borgo_altinate,
            R.array.position_9_caffè_pedrocchi,
            R.array.position_8_cappella_scrovegni,
            R.array.position_3_galileo_house,
            R.array.position_7_chiesa_eremitani,
            R.array.position_4_chiesa_san_francesco,
            R.array.position_16_chiesa_san_nicolò,
            R.array.position_19_chiesa_san_pietro,
            R.array.position_20_chiesa_san_tomaso,
            R.array.position_5_chiesa_santa_sofia,
            R.array.position_14_oratorio_san_rocco,
            R.array.position_10_orologio_civile,
            R.array.position_12_palazzo_ragione,
            R.array.position_18_piazza_capitaniato,
            R.array.position_15_piazza_signori,
            R.array.position_1_prato_della_valle
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
