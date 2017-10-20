package com.wcs.gcaule.geolocalisation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LOCALISATION_TAG";
    private LocationManager mLocationManager = null;
    private LocationListener mLocationListener = null;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int PERMISSION_REQUEST_LOCALISATION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Acquire a reference to the system Location Manager
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                String longitude = Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
                String latitude = Location.convert(location.getLatitude(), Location.FORMAT_DEGREES);

                // Called when a new location is found by the network location provider.
                Toast.makeText(MainActivity.this,
                        "Longitude : " + longitude + "\nLatitude : " +  latitude,
                        Toast.LENGTH_SHORT).show();

                ((TextView) findViewById(R.id.localisation)).setText("Longitude : "
                        + longitude + "\nLatitude : " +  latitude);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {
                Log.i(TAG, "onProviderEnabled: ");
            }

            public void onProviderDisabled(String provider) {
                Log.i(TAG, "onProviderDisabled: ");
            }
        };
    }

    private void checkPermission() {

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    PERMISSIONS, PERMISSION_REQUEST_LOCALISATION);
            return;
        }

        String provider = mLocationManager.getBestProvider(new Criteria(), false);
        Location location = mLocationManager.getLastKnownLocation(provider);

        if (location != null) {

            String longitude = Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
            String latitude = Location.convert(location.getLatitude(), Location.FORMAT_DEGREES);

            Toast.makeText(MainActivity.this,
                    "Longitude : " + longitude + "\nLatitude : " +  latitude, Toast.LENGTH_SHORT).show();

            ((TextView) findViewById(R.id.localisation)).setText("Longitude : "
                    + longitude + "\nLatitude : " +  latitude);
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_LOCALISATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this,
                            getResources().getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();

                    checkPermission();

                } else {

                    Toast.makeText(MainActivity.this,
                            getResources().getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}