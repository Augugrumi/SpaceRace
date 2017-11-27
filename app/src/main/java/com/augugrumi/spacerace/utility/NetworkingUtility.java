package com.augugrumi.spacerace.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.augugrumi.spacerace.SpaceRace;
import com.augugrumi.spacerace.listener.NetworkChangeListener;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 27/11/17
 */

public class NetworkingUtility extends BroadcastReceiver {
    private static boolean connectivityAvailable;
    private static NetworkChangeListener listener;

    static {
        connectivityAvailable = connectivityCheck();
    }

    public static boolean isNetworkAvailable() {
        return connectivityAvailable;
    }

    private static boolean connectivityCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                SpaceRace.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public static void registerListener(NetworkChangeListener listener) {
        NetworkingUtility.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        connectivityAvailable = connectivityCheck();

        Log.d("NETWORKINGUTILITY", "NETWORKINGUTILITY " + connectivityAvailable);
        listener.onNetworkAvailable();
    }
}
