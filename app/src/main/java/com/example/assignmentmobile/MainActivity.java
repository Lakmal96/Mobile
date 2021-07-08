package com.example.assignmentmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000;
    private final long MIN_DISTANCE = 5;
    private LatLng latLng;

    String currentLongtitude;
    String currentLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        getLocation();

        Button stopSend = findViewById(R.id.stop);
        stopSend.setEnabled(false);
    }

    public void alertStop(View view) {
        Button sosBtn = findViewById(R.id.sos);
        sosBtn.setEnabled(true);

        Button buttonStop = findViewById(R.id.stop);
        buttonStop.setEnabled(false);
    }

    public void sendSOS(View view) {
        Button sosBtn = findViewById(R.id.sos);
        sosBtn.setEnabled(false);

        Button buttonStop = findViewById(R.id.stop);
        buttonStop.setEnabled(true);

        try {
            getLocation();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to send the location: " + e, Toast.LENGTH_LONG).show();
        }

        try {
            String mobileNo = "0716332197";
            String message = "IM/2017/044 http://maps.google.com/?q=" + currentLongtitude + "," + currentLatitude;

            if (!mobileNo.equals("")) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(mobileNo, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed" + e, Toast.LENGTH_LONG).show();
        }
    }


    public void getLocation() {

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    currentLatitude=Double.toString(location.getLatitude());
                    currentLongtitude=Double.toString(location.getLongitude());
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        };


        try {
            locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,locationListener);
        }catch (SecurityException e){e.printStackTrace();
        }
    }
}