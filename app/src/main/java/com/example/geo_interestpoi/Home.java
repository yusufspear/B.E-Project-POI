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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Home extends AppCompatActivity implements OnMapReadyCallback {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        SupportMapFragment supportMapFragment= SupportMapFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment,supportMapFragment)
                .commit();

        supportMapFragment.getMapAsync(this);
        initViews();
//
//        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_home);
//        supportMapFragment.getMapAsync((OnMapReadyCallback) this);


//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                Fragment selectedFragment = new FragmentHome();
//
//                switch (menuItem.getItemId()) {
//                    case R.id.home:
//                        break;
//                    case R.id.search:
//                        selectedFragment = new FragmentSearch();
//                        break;
//                    case R.id.history:
//                        selectedFragment = new FragmentHistory();
//                        break;
//                    case R.id.profile:
//                        selectedFragment = new FragmentProfile();
//                        break;
//                }
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home,
//                        selectedFragment).commit();
//
//                return true;            }
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

    private void initViews() {

        bottomNavigationView=findViewById(R.id.bottom_navigation_bar);
//        FragmentHome = findViewById(R.id.fragment_home);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Map", "IT is Ready");
    }
}
