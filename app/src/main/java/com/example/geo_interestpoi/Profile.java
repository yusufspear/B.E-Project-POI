package com.example.geo_interestpoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    Button signout;
    private TextView mOutputText;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
//    FrameLayout FragmentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        mOutputText.setText( user.getEmail());

        signout.setOnClickListener(this::signOutUser);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.profile:
                        return true;

                    case R.id.home:
                        startActivity(new Intent(Profile.this, Home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(Profile.this, Search.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.history:
                        startActivity(new Intent(Profile.this, History.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }



        private void signOutUser(View view){
            mAuth.signOut();
            startActivity(new Intent(this,MainActivity.class));
            //updateUI();

        }

    private void initViews() {

        bottomNavigationView=findViewById(R.id.bottom_navigation_bar);
        signout=findViewById(R.id.btn_singoutuser);
        mOutputText=findViewById(R.id.txt1);

//        FragmentHome = findViewById(R.id.fragment_home);

    }


}
