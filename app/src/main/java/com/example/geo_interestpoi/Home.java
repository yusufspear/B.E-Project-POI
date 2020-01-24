package com.example.geo_interestpoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
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


public class Home extends AppCompatActivity implements OnMapReadyCallback {

    private static final double Lang= 19.077806;
    private static final double Long= 72.883056;
    GoogleMap mMap;
    FloatingActionButton MyLocation;
    FrameLayout frameLayout;
    EditText mAddress;
    ImageButton mSearch;
    BottomNavigationView bottomNavigationView;
    private FusedLocationProviderClient mLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GoogleMapOptions options= new GoogleMapOptions()
                .zoomControlsEnabled(true)
                .mapToolbarEnabled(true);


        SupportMapFragment supportMapFragment= SupportMapFragment.newInstance(options);
        mLocationClient = new FusedLocationProviderClient(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment,supportMapFragment)
                .commit();

        supportMapFragment.getMapAsync(this);
        initViews();
        mSearch.setOnClickListener(this::goLocation);
        MyLocation.setOnClickListener(this::CurrentLocation);

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

        if (mMap!=null){

            mLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.isSuccessful()){
                        Location address=task.getResult();
                        location(address.getLatitude(),address.getLongitude());
//                        mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),address.getLongitude())));

                        mMap.animateCamera(CameraUpdateFactory.zoomTo(3.0f));
                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latLng,19);

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
                    else {

                        Toast.makeText(Home.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            });


        }
    }

    private void initViews() {

        bottomNavigationView=findViewById(R.id.bottom_navigation_bar);
        mAddress =findViewById(R.id.input);
        mSearch =findViewById(R.id.imgbtn_search);
        frameLayout = findViewById(R.id.map_fragment);
        MyLocation = findViewById(R.id.fab_myLocation);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d("Map", "IT is Ready");
        mMap =googleMap;
        location(Lang,Long);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setMyLocationEnabled(true);

    }

    private void location(double Lang, double Long) {

        LatLng latLng=new LatLng(Lang,Long);
        showMarker(latLng);
        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(latLng,18);
        mMap.moveCamera(cameraUpdate);

    }

    private void showMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);
    }
}
