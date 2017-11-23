package com.example.sea.googlemap;

import android.Manifest;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap gMap;

    protected static final String TAG = "MainActivity";

    protected Location mLastLocation;

    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView mAddressText;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int RC_LOCATION = 1;
    private static final int RC_LOCATION_UPDATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*콜백 인스턴스 설정*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //
        mFusedLocationClient
                = LocationServices.getFusedLocationProviderClient(this);
        mLatitudeText = (TextView)findViewById(R.id.latitude);
        mLongitudeText = (TextView)findViewById(R.id.longititude);
        final EditText edittext = (EditText)findViewById(R.id.edittext);
        getLastLocation();

        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findLocation(edittext.getText().toString());
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @SuppressWarnings("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION) // automatically re-invoke this method after getting the required permission
    public void getLastLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastLocation = task.getResult();
                                updateUI();
                            } else {
                                Log.w(TAG, "getLastLocation:exception", task.getException());
                                //showSnackbar(getString(R.string.no_location_detected));
                            }
                        }
                    });
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,
                    "This app needs access to your location to know where you are.",
                    RC_LOCATION, perms);
        }
    }
    /*업데이트 메소드*/
    protected void updateUI() {
        mLatitudeText.setText(String.format( "%f",
                mLastLocation.getLatitude()));
        mLongitudeText.setText(String.format("%f",
                mLastLocation.getLongitude()));
        float a = Float.parseFloat(String.format( "%f",
                mLastLocation.getLatitude()));
        float b = Float.parseFloat(String.format("%f",
                mLastLocation.getLongitude()));
        LatLng sydney = new LatLng(a, b);
        gMap.addMarker(new MarkerOptions().position(sydney).title("this"));
        // move the camera
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));


    }
    /*특정 위치 검색*/
    private void findLocation(String input){
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(input,1);
            if(addresses.size()>0){
                Address bestResult = (Address) addresses.get(0);
                float a = Float.parseFloat(String.format("%s",bestResult.getLatitude()));
                float b = Float.parseFloat(String.format("%s",bestResult.getLongitude()));
                LatLng sydney = new LatLng(a, b);
                gMap.addMarker(new MarkerOptions().position(sydney).title(input));
                // move the camera
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
            }
        } catch (IOException e) {
            Log.i("유","감");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        gMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // move the camera
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
