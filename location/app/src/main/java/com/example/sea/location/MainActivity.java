package com.example.sea.location;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.widget.Toast;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    ArrayList<Listviewitem> item = new ArrayList<>();
    ListAdapter adapter;

    private static final int RC_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected LocationCallback mLocationCallback;//주기적 업데이트
    EditText editText;
    ListView listView;
    int trackinCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        /*listview*/
        adapter = new ListAdapter(this, R.layout.item, item);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        item.add(new Listviewitem("test","t"));
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = (EditText)findViewById(R.id.edittext);
                if(editText.getText().toString().equals("현재위치")) {//현재 위치 검색
                    getLastLocation();
                }else if(editText.getText().toString().equals("위치추적")){//콜백을 이용한 지속적 위치 업데이트
                    trackinCount = 0;
                    startLocationUpdates();
                }else{//특정 위치검색
                    findLocation(editText.getText().toString());
                }
            }
        });
        ///////////////
    }
    /*권한 획득*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    /*현재 위치 검색*/
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
                                Log.w("zz", "getLastLocation:exception", task.getException());
                                showSnackbar(getString(R.string.no_location_detected));
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

    /*주기적 현재 위치 업데이트*/
    private void startLocationUpdates() {
        LocationRequest locRequest = new LocationRequest();
        locRequest.setInterval(10000);//최대 10초
        locRequest.setFastestInterval(5000);//최소 5초
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLastLocation = locationResult.getLastLocation();
                updateUI();
            }
        };
        mFusedLocationClient.requestLocationUpdates(locRequest, mLocationCallback, Looper.myLooper());//콜백
    }
    public void stopLocationUpdate() {//주기적 업데이트 중단
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    /*특정 위치 검색*/
    private void findLocation(String input){
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(input,1);
            if(addresses.size()>0){
                if(listView.getCount()>=10){
                    adapter.remove();//10개 넘을시 0번째 인덱스 삭제
                }
                Address bestResult = (Address) addresses.get(0);
                item.add(new Listviewitem(String.format("%s",bestResult.getLatitude()),String.format("%s",bestResult.getLongitude())));
                adapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            Log.i("유","감");
        }
    }

    /*업데이트 메소드*/
    protected void updateUI() {
        if(trackinCount>10){
            stopLocationUpdate();//주기적 업데이트 중단
        }else if(listView.getCount()>=10){
            adapter.remove();//10개 넘을시 0번째 인덱스 삭제
        }

        item.add(new Listviewitem(String.format(Locale.ENGLISH, "%f",
                mLastLocation.getLatitude()),
                String.format(Locale.ENGLISH, "%f",
                        mLastLocation.getLongitude())
        ));
        adapter.notifyDataSetChanged();
    }
    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
        }
    }
}
