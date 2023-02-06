package com.school.trx.LocationService;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import java.util.List;

public class Util {

    @SuppressWarnings("deprecation")
    static public boolean isLocationServiceRunning(Activity a) {
        ActivityManager m = (ActivityManager) a.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serv = m.getRunningServices(Integer.MAX_VALUE);
        for (int i=0; i<serv.size(); i++) {
            Log.d("SERVICE", serv.get(i).toString());
            if (serv.get(i).getClass().getName().equals(LocationService.class.getName())) {
                return true;
            }
        }

        return false;
    };

    static public boolean isGPSEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) == true || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true;
    }
}
