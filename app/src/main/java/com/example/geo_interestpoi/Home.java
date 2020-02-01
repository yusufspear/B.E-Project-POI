package com.example.geo_interestpoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Home extends AppCompatActivity implements OnMapReadyCallback {

    private static final double Lang= 19.077806;
    private static final double Long= 72.883056;
    private static final int GPS_REQUEST_CODE =9001;
    GoogleMap mMap;
    FloatingActionButton mCurrentLocation;
    FrameLayout frameLayout;
    EditText mAddress;
    ImageButton mSearch;
    BottomNavigationView bottomNavigationView;
    private FusedLocationProviderClient mLocationClient;
    private LocationCallback mLocationCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GoogleMapOptions options= new GoogleMapOptions()
                .zoomControlsEnabled(true)
                .mapToolbarEnabled(true);

        SupportMapFragment supportMapFragment= SupportMapFragment.newInstance(options);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment,supportMapFragment)
                .commit();

        supportMapFragment.getMapAsync(this);
        initViews();
        mSearch.setOnClickListener(this::goLocation);
        mCurrentLocation.setOnClickListener(this::CurrentLocation);

        mLocationClient= new FusedLocationProviderClient(this);

        mLocationCallback = new  LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.v("locF", "onLocationResult: Execute");

                if (locationResult == null){
                    Log.i("locNR", "onLocationResult: Execute");
                    return;
                }
                Log.i("locR", "onLocationResult: Execute");
                Location address= locationResult.getLastLocation();
//                    LocationUpdate();
                Toast.makeText(Home.this,"Lat:- " +address.getLatitude()+"\n Long:- "+address.getLongitude(),Toast.LENGTH_LONG).show();

                mMap.animateCamera(CameraUpdateFactory.zoomTo(3.0f));
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latLng,19);

                mMap.animateCamera(cameraUpdate, 1000, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
//                        Toast.makeText(Home.this, "Finished", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
//                        Toast.makeText(Home.this, "Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        };

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.home:
                        return true;

                    case R.id.search:
                        startActivity(new Intent(Home.this, Search.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.history:
                        startActivity(new Intent(Home.this, History.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(Home.this, Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }

    private void goLocation(View view) {

        String input =mAddress.getText().toString();
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address>  addressList=geocoder.getFromLocationName(input,10);

            if (addressList.size()>0){

                Address address=addressList.get(0);
                location(address.getLatitude(),address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),address.getLongitude())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CurrentLocation(View view) {


        LocationUpdate();



//
//        if (isGPSEnabled()){
//
//            mLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    if (task.isSuccessful()){
//                        Location address = task.getResult();
//                        location(address.getLatitude(),address.getLongitude());
//
//                    }else {
//                        Log.i("Task ", "onComplete: Not Execute");
//                    }
//                }
//            });
//
//        }




//        if (mMap!=null){
//
//            mLocationCallback = new  LocationCallback(){
//
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    Log.i("locF", "onLocationResult: Execute");
//
//                    if (locationResult == null){
//                        Log.i("locNR", "onLocationResult: Execute");
//                        return;
//                    }
//                    Log.i("locR", "onLocationResult: Execute");
//                    Location location= locationResult.getLastLocation();
////                    LocationUpdate();
//                    Toast.makeText(Home.this,"Lat:- " +location.getLatitude()+"\n Long:- "+location.getLongitude(),Toast.LENGTH_LONG).show();
//
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(3.0f));
//                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//                    CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latLng,19);
//
//                    mMap.animateCamera(cameraUpdate, 1000, new GoogleMap.CancelableCallback() {
//                        @Override
//                        public void onFinish() {
//                            Toast.makeText(Home.this, "Finished", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            Toast.makeText(Home.this, "Cancelled", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                }
//            };
//
////            mLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
////                @Override
////                public void onComplete(@NonNull Task<Location> task) {
////
////                    if (task.isSuccessful()){
////                        Location address=task.getResult();
////                        location(address.getLatitude(),address.getLongitude());
//////                        mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),address.getLongitude())));
////
////                        mMap.animateCamera(CameraUpdateFactory.zoomTo(3.0f));
////                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
////                        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latLng,19);
////
////                        mMap.animateCamera(cameraUpdate, 1000, new GoogleMap.CancelableCallback() {
////                            @Override
////                            public void onFinish() {
////                                Toast.makeText(Home.this, "Finished", Toast.LENGTH_SHORT).show();
////                            }
////
////                            @Override
////                            public void onCancel() {
////                                Toast.makeText(Home.this, "Cancelled", Toast.LENGTH_SHORT).show();
////
////                            }
////                        });
////
////                    }
////                    else {
////
////                        Toast.makeText(Home.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
////                    }
////
////                }
////            });
//
//
//        }
    }
    private  boolean isGPSEnabled(){

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnable  = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnable){
            return true;
        }else {
            Context context;
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permission")
                    .setMessage("It is Required For Finding Current Location of the Device")
                    .setCancelable(false)
                    .setPositiveButton("Yes",(dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent,GPS_REQUEST_CODE);
                    })
                    .show();
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GPS_REQUEST_CODE){

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean providerEnable  = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (providerEnable) {
                Toast.makeText(this, "GPS Enabled", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"GPS NOT Enabled",Toast.LENGTH_LONG).show();

            }
        }
    }

    private void initViews() {

        bottomNavigationView=findViewById(R.id.bottom_navigation_bar);
        mAddress =findViewById(R.id.input);
        mSearch =findViewById(R.id.imgbtn_search);
        frameLayout = findViewById(R.id.map_fragment);
        mCurrentLocation = findViewById(R.id.fab_myLocation);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d("Map", "IT is Ready");
        mMap =googleMap;
        location(Lang,Long);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setMyLocationEnabled(true);

    }

    private void LocationUpdate(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        mLocationClient.requestLocationUpdates(locationRequest,mLocationCallback, Looper.getMainLooper());
    }

    private void location(double Lang, double Long) {

        LatLng latLng=new LatLng(Lang,Long);
        showMarker(latLng);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(3.0f));
        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(latLng,19);
                mMap.animateCamera(cameraUpdate, 1000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(Home.this, "Finished", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Home.this, "Cancelled", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);
    }
}
