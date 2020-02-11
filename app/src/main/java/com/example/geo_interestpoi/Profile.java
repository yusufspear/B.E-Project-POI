package com.example.geo_interestpoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    Button signout;
    private TextView mOutputText;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View doorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            doorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );
        }
        setContentView(R.layout.activity_profile);
        initViews();
        mAuth=FirebaseAuth.getInstance();
//        FirebaseUser user=mAuth.getCurrentUser();
        String[] New = new String[6];
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("User");

        mRef.child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);
                New[0] = dataSnapshot.child("isnew").getValue(String.class);
                New[1] = dataSnapshot.child("email").getValue(String.class);

                Log.i("List", "onDataChange: Execute");
                mOutputText.setText(user.getGender() + " HI");
                Log.i("List", "onDataChange: Execute1");

                mOutputText.setText(New[0] + " HI\n" + New[1]);
//
//                if (New[0].equals("false")){
//                    Log.i("if Ex", "onCreate: if Exiciye");
//                    mOutputText.setText(New[0] + " HI");
//
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




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
