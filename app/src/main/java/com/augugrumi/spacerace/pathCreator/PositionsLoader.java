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
            R.array.prato_valle,
            R.array.verso_santa_lucia
    };

    public static List<LatLng> getPositions () {

        Resources r = SpaceRace.getAppContext().getResources();

        List<LatLng> buildingsPositions = new ArrayList<>();
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.prato_valle)[0]),
                Double.parseDouble(r.getStringArray(R.array.prato_valle)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.basilica_sant_antonio)[0]),
                Double.parseDouble(r.getStringArray(R.array.basilica_sant_antonio)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.battistero_duomo)[0]),
                Double.parseDouble(r.getStringArray(R.array.battistero_duomo)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.bo)[0]),
                Double.parseDouble(r.getStringArray(R.array.bo)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.borgo_altinate)[0]),
                Double.parseDouble(r.getStringArray(R.array.borgo_altinate)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.caffe_pedrocchi)[0]),
                Double.parseDouble(r.getStringArray(R.array.caffe_pedrocchi)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.cappella_degli_scrovegni)[0]),
                Double.parseDouble(r.getStringArray(R.array.cappella_degli_scrovegni)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.casa_galileo)[0]),
                Double.parseDouble(r.getStringArray(R.array.casa_galileo)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.chiesa_degli_eremitani)[0]),
                Double.parseDouble(r.getStringArray(R.array.chiesa_degli_eremitani)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.chiesa_san_francesco)[0]),
                Double.parseDouble(r.getStringArray(R.array.chiesa_san_francesco)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.chiesa_san_tommaso)[0]),
                Double.parseDouble(r.getStringArray(R.array.chiesa_san_tommaso)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.chiesa_san_nicolo)[0]),
                Double.parseDouble(r.getStringArray(R.array.chiesa_san_nicolo)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.chiesa_santa_sofia)[0]),
                Double.parseDouble(r.getStringArray(R.array.chiesa_santa_sofia)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.chiesa_san_pietro)[0]),
                Double.parseDouble(r.getStringArray(R.array.chiesa_san_pietro)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.orologio_civile)[0]),
                Double.parseDouble(r.getStringArray(R.array.orologio_civile)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.verso_santa_lucia)[0]),
                Double.parseDouble(r.getStringArray(R.array.verso_santa_lucia)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.borgo_altinate)[0]),
                Double.parseDouble(r.getStringArray(R.array.borgo_altinate)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.oratorio_san_rocco)[0]),
                Double.parseDouble(r.getStringArray(R.array.oratorio_san_rocco)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.palazzo_della_ragione)[0]),
                Double.parseDouble(r.getStringArray(R.array.palazzo_della_ragione)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.piazza_capitaniato)[0]),
                Double.parseDouble(r.getStringArray(R.array.piazza_capitaniato)[1])
        ));
        buildingsPositions.add(new LatLng(
                Double.parseDouble(r.getStringArray(R.array.piazza_dei_signori)[0]),
                Double.parseDouble(r.getStringArray(R.array.piazza_dei_signori)[1])
        ));

        return buildingsPositions;
    }
}
