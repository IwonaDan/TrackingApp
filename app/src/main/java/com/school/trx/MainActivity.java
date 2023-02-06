package com.school.trx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.school.trx.LocationService.LocationService;
import com.school.trx.LocationService.Util;

public class MainActivity extends PermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private LocationService mServiceObj;
    private Intent mServiceIntent;
    @Override
    public void onPermissionGranted() {
        Log.d("PGGGGG", "PGGGGGG");

        mServiceObj = new LocationService();
        mServiceIntent = new Intent(this, mServiceObj.getClass());
        startService(mServiceIntent);

        Boolean b = Util.isLocationServiceRunning(this);
        Log.d("GPS SERVICE ", b.toString());

    }
}