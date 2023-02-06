package com.school.trx.LocationService;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.nio.channels.Channel;

/*
    https://stackoverflow.com/questions/63920240/service-is-not-giving-location-updates
    You need to add an attribute android:foregroundServiceType="location" to the service so it's
    running in the foreground when app is NOT active. The service will continue to run even after
    the app is closed.

    If you don't add it, the service will continue to run only if app is active due to battery saving
    features of new Android APIs.
*/

public class LocationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChanel();
        requestLocationUpdates();
    }

    private int counter = 0;
    private void createNotificationChanel() {
        String NOTIFICATION_CHANNEL_ID = "com.getlocationbackground";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification n = b.setOngoing(true)
                .setContentTitle("App is running count::" + counter)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, n);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void requestLocationUpdates() {

        //LocationRequest request = (new LocationRequest.Builder(10000)).setQuality(LocationRequest.QUALITY_HIGH_ACCURACY).build();
        LocationRequest request = LocationRequest.create().setInterval(10000).setFastestInterval(5000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int perm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (perm != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationCallback l_callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d("Location Service", "location update "+latitude+"/"+longitude);
                } else {
                    Log.d("Location Service", "location is empty");
                }
            }
        };

        client.requestLocationUpdates(request, l_callback, null);
    }
}

