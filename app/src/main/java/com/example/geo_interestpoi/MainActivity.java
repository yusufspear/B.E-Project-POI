package com.example.geo_interestpoi;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.SearchRecentSuggestions;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout mEmailLayout, mPasswordLayout;
    private Button mBtnSignin, mBtnRegisterUser, mBtnSignoutUser;
    private TextView mOutputText,txtForgetPassword,txtSign_up;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private int i,j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("User");
//        updateUI();
        initViews();

        txtForgetPassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtSign_up.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        mBtnSignin.setOnClickListener(this::singInUser);
        txtSign_up.setOnClickListener(this::signUp);
        txtForgetPassword.setOnClickListener(this::forgetPassword);
        hideProgressBar();
//



        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        if (isCheck()){

                            Toast.makeText(MainActivity.this, "Permission Granted Successfully", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    updateUI();
            }
        };


    }

    private void forgetPassword(View view) {
        startActivity(new Intent(this,ForgetPassword.class));
        finish();
    }

    private boolean isCheck() {

        if (i == 1){
            i=0;
            return true;
        }else{
            return false;
        }

    }

    private void signUp(View view) {

        Intent i=new Intent(this,Register_Page.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    private void singInUser(View view) {

        if (!validateEmailAddress() | !validatePassword()) {
            // Email or Password not valid,
//            Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
            return ;
        }
        //Email and Password valid, sign in user here

        String email=mEmailLayout.getEditText().getText().toString().trim();
        String password=mPasswordLayout.getEditText().getText().toString().trim();
//        String[] ans = new String[2];
//        ans[0]= "1";
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){

                                mRef.child(mAuth.getCurrentUser().getUid()).child("isnew").addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Log.i("Permission","On Data Change Field Execute");
                                        String isNew;
                                        isNew = dataSnapshot.getValue(String.class);

                                        if (isNew.equals("true")){
                                            startActivity(new Intent(MainActivity.this, POI_Set.class));
                                            Toast.makeText(MainActivity.this,"User Log-in Successfully Please Select POI",Toast.LENGTH_LONG).show();
                                            finish();

                                        }else{
//                                            updateUI();
                                            startActivity(new Intent(MainActivity.this, Home.class));
                                            Toast.makeText(MainActivity.this,"User Log-in Successfully",Toast.LENGTH_LONG).show();


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                        Log.e("Error", databaseError.getMessage());
                                    }
                                });
                            }

                            else{
                                Toast.makeText(MainActivity.this,"Please Verify the Email Address",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            hideProgressBar();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(MainActivity.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                                mOutputText.setText("Invalid Password");
                            }else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(MainActivity.this,"Email not Exit in Database",Toast.LENGTH_SHORT).show();
                                mOutputText.setText("Email NOT Exit");
                            }
                        }


                }


                });

    }

    private void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();
        showProgressBar();
        if (user != null && mAuth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(this,Home.class));

        }


        else{
                mOutputText.setText("User NOT Log-in");

            }


    }

//    private void createUser(View view) {
//
//        if (!validateEmailAddress() | !validatePassword()) {
//            // Email or Password not valid,
//            return;
//        }
//        //Email and Password valid, create user here
//
//        String email=mEmailLayout.getEditText().getText().toString().trim();
//        String password=mPasswordLayout.getEditText().getText().toString().trim();
//        showProgressBar();
//
//        mAuth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(MainActivity.this,"User Created Successfully",Toast.LENGTH_SHORT).show();
//                            hideProgressBar();
//                            startActivity(new Intent(MainActivity.this,MapsActivity.class));
//                            //updateUI();
//                        }else{
//                            Toast.makeText(MainActivity.this,"Error Occur",Toast.LENGTH_SHORT).show();
//                            hideProgressBar();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        hideProgressBar();
//                        if (e instanceof FirebaseAuthUserCollisionException){
//                            Toast.makeText(MainActivity.this,"Email Already in Database",Toast.LENGTH_SHORT).show();
//                            mOutputText.setText("Email Already in Database");
//                        }
//                    }
//                });
//
//
//    }
//
//    private void signOutUser(View view){
//        mAuth.signOut();
//        //updateUI();
//
//    }

    private void initViews() {
        mEmailLayout = findViewById(R.id.et_email);
        mPasswordLayout = findViewById(R.id.et_password);
        mBtnSignin = findViewById(R.id.btn_singin);
//        mBtnRegisterUser = findViewById(R.id.btn_registeruser);
        mOutputText = findViewById(R.id.tv_output);
        mProgressBar = findViewById(R.id.progressbar);
//        mBtnSignoutUser = findViewById(R.id.btn_singoutuser);
        txtSign_up = findViewById(R.id.txtsign_up);
        txtForgetPassword = findViewById(R.id.txtforget);

    }

    private boolean validateEmailAddress() {

        String email = mEmailLayout.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            mEmailLayout.setError("Email is required. Can't be empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailLayout.setError("Invalid Email. Enter valid email address.");
            return false;
        } else {
            mEmailLayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        String password = mPasswordLayout.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            mPasswordLayout.setError("Password is required. Can't be empty.");
            return false;
        } else {
            mPasswordLayout.setError(null);
            return true;
        }
    }

    private void showProgressBar() {
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    protected void onResume(){
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            if (mAuth == null) {
                mAuth.removeAuthStateListener(mAuthStateListener);
            }
        }
    }
}

