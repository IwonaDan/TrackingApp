package com.school.trx;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public abstract class PermissionActivity extends AppCompatActivity {

    private Boolean granted = false;
    private String phoneNumber = "";

    private boolean CheckPermissions(boolean request) {
        boolean gps_working = false;
        boolean number_working = false;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED) {
            number_working = true;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gps_working = true;
        }

        if (!gps_working || !number_working) {
            if (request) {
                List<String> req = new ArrayList<String>();
                if (!gps_working) {
                    req.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (!number_working) {
                    req.add(Manifest.permission.READ_PHONE_NUMBERS);
                }
                requestPermissions(req.toArray(new String[0]), 0x000a);
            }
            return false;
        }

        TelephonyManager mManager =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        phoneNumber = mManager.getLine1Number();
        granted = true;
        onPermissionGranted();
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckPermissions(true);

        Log.d("# Basic", this.getPhoneNumber());
        Log.d("# Basic", this.getGranted().toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckPermissions(false);

        if (!getGranted()) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please approve PRECISE Geolocation permissions for the app");
            dlgAlert.setTitle("Permission issues");
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Boolean getGranted() {
        return granted;
    }

    abstract public void onPermissionGranted();
}
