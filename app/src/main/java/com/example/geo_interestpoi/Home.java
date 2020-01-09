package com.example.geo_interestpoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Home extends AppCompatActivity implements OnMapReadyCallback {

    private static final double Lang= 19.077806;
    private static final double Long= 72.883056;
    GoogleMap mMap;
    FloatingActionButton MyLocation;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
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

    private void CurrentLocation(View view) {

        if (mMap!=null){

            mMap.animateCamera(CameraUpdateFactory.zoomTo(3.0f));
            LatLng latLng = new LatLng(Lang, Long);
            CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latLng, 20);

            mMap.animateCamera(cameraUpdate, 3000, new GoogleMap.CancelableCallback() {
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
    }

    private void initViews() {

        bottomNavigationView=findViewById(R.id.bottom_navigation_bar);
        frameLayout = findViewById(R.id.map_fragment);
        MyLocation = findViewById(R.id.fab_myLocation);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d("Map", "IT is Ready");
        mMap =googleMap;
        location(Lang,Long);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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
