package com.example.vivekgsheetproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    //Define Local variables
    EditText edit_text_edit, edit_text_edit_2;
    Button buttonAddItem2;

    public GoogleMap mMap;
    private Marker mCurrLocationMarker;
    private DeviceFusedLocation deviceFusedLocation;
    private LocationManager deviceLocationManager;
    private DeviceLocationListener deviceLocationListener;
    private LocationRequest deviceLocationRequest;
    private LocationCallback deviceLocationCallback;
    String latti;
    String longi;





    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize local variables

        deviceLocationRequest = new LocationRequest();
        deviceLocationListener = new DeviceLocationListener();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        edit_text_edit = (EditText)findViewById(R.id.text_edit);
        edit_text_edit_2 = (EditText)findViewById(R.id.Total_Delivery);

        /*buttonAddItem2 = (Button)findViewById(R.id.logout);
        buttonAddItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToSheet();
                Log.e("RAK","Button clicked");
                finish();
            }
        });*/

        //Request the user to enable location
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Log.e("RAK", "Requested...");
        deviceLocationRequest.setInterval(100);
        deviceLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng deviceCurrentLatlng = new LatLng(latitude, longitude);
                    if (mMap != null) {
                        if (mCurrLocationMarker != null) {
                            mCurrLocationMarker.remove();
                        }
                        mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                                .position(deviceCurrentLatlng)
                                .title("fused gps")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        );
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(deviceCurrentLatlng));
                    } else {
                        Log.e("RAK", "gMap not ready");
                    }
                    Log.e("RAKE", new Double(location.getLatitude()).toString() + "," + new Double(location.getLongitude()).toString());
                    latti = new Double(location.getLatitude()).toString();
                    longi = new Double(location.getLongitude()).toString();
                    locator(latti, longi);




                }
            }

        };
        deviceFusedLocation = new DeviceFusedLocation(getApplicationContext());
        deviceFusedLocation.requestLocationUpdates(deviceLocationRequest, deviceLocationCallback, Looper.getMainLooper());

        //------------------------------------

        deviceLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
        deviceLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, deviceLocationListener);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(requestCode == 1) Log.e("RAK","Permission code");
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        deviceLocationListener.updateMap(mMap);
        Log.e("RAK","Map ready!");

    }
    //-----
    private void  addItemToSheet () {

        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        final String km_out = edit_text_edit.getText().toString().trim();
        final String Td = edit_text_edit_2.getText().toString().trim();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxteODThV9UZ3zfmCu10jD1UUA0KIBI7Z6lfA0RpAMH2Dg-Ap8/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(MapsActivity.this,response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addItem");
                parmas.put("SiteNo",new Integer(00).toString());
                parmas.put("EmployeeId",new Integer(00).toString());
                parmas.put("VehicleNo",new Integer(00).toString());
                parmas.put("MobileNo",new Integer(00).toString());
                parmas.put("KmEntry",new Integer(00).toString());
                parmas.put("KmOut",km_out);
                parmas.put("TotalDelivery",Td);

                return parmas;

            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }
    // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    private void   locator(String latti, String longi) {


        //final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        //final String km_out = edit_text_edit.getText().toString().trim();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbx7MuJPfX1gJdn9kSQjwSrM2sxMnCbGludCw6GFEQcMo1Hahgf4/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //loading.dismiss();
                        //Toast.makeText(MapsActivity.this,response,Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        //startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addItem");
                parmas.put("Latti", MapsActivity.this.latti);
                parmas.put("Longi", MapsActivity.this.longi);
                buttonAddItem2 = (Button)findViewById(R.id.logout);
                buttonAddItem2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addItemToSheet();
                        Log.e("RAK","Button clicked");
                        finish();
                    }
                });

                return parmas;



            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }
    //-----

    @Override
    public void onClick(View v) {

    }
}
