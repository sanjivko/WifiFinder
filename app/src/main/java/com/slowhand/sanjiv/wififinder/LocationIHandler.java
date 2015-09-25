package com.slowhand.sanjiv.wififinder;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by sanjiv on 18/9/15.
 */
public class LocationIHandler {

    String currentLocn = "";
    private LocationManager sysLocnManager;
    private MainActivity mainActivity;
    private HttpConnector httpConn;

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocn = location.getLongitude() + ":" + location.getLatitude();
            TextView textViewLocn = (TextView) mainActivity.findViewById(R.id.textViewLocn);
            textViewLocn.setText(currentLocn);

            Log.d("WiFinder", "Current Location:" + currentLocn);


            httpConn.getSSID_by_location(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("WiFinder", "Current Provider:" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public LocationIHandler(MainActivity act, LocationManager mgr) {
        sysLocnManager = mgr;
        mainActivity = act;
        httpConn = new HttpConnector(mainActivity);
        try {
            mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 200, locationListener);
            mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 200, locationListener);
        } catch (SecurityException ex) {
            Log.w("WiFinder", "Permission to access location is not enabled");
        }
    }

    public String getLocation() {
        return "";
    }
}
