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
