package com.example.vivekgsheetproject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DeviceLocationListener implements LocationListener {
    private Marker mCurrLocationMarker;
    public GoogleMap gMap;

    public void updateMap(GoogleMap gMap){
        this.gMap = gMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("RAK",new Double(location.getLatitude()).toString()+","+new Double(location.getLongitude()).toString());
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng deviceCurrentLatlng = new LatLng(latitude, longitude);
        if(gMap!=null) {
            if(mCurrLocationMarker!=null){
                mCurrLocationMarker.remove();
            }
            mCurrLocationMarker = gMap.addMarker(new MarkerOptions().position(deviceCurrentLatlng).title("normal gps"));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(deviceCurrentLatlng));
        }else {
            Log.e("RAK","gMap not ready");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
