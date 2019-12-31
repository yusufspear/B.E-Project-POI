package com.example.geo_interestpoi;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {

    private TextInputLayout mEmailLayout, mPasswordLayout;
    private Button mBtnSignin, mBtnRegisterUser, mBtnSignoutUser;
    private TextView mOutputText,txtForgetPassword,txtSign_up;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        txtForgetPassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtSign_up.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        mAuth=FirebaseAuth.getInstance();

        mBtnSignin.setOnClickListener(this::singInUser);
//        mBtnRegisterUser.setOnClickListener(this::createUser);
        mBtnSignoutUser.setOnClickListener(this::signOutUser);
        txtSign_up.setOnClickListener(this::signUp);

        hideProgressBar();
//        updateUI();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUI();
            }
        };

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
            Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
            return ;
        }
        //Email and Password valid, sign in user here

        String email=mEmailLayout.getEditText().getText().toString().trim();
        String password=mPasswordLayout.getEditText().getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            hideProgressBar();
                            Toast.makeText(MainActivity.this,"User Log-in Successfully",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(i);
                            updateUI();
                        }else{
                            hideProgressBar();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(MainActivity.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                                mOutputText.setText("Invalid Password");
                            }else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(MainActivity.this,"Email not Exit in Database",Toast.LENGTH_SHORT).show();
                                mOutputText.setText("Email not Exit in Database");
                            }
                        }
                    }
                });

    }

    private void updateUI() {

        FirebaseUser user=mAuth.getCurrentUser();
        if (user == null){
            mOutputText.setText("User NOT Log-in");
            return;
        }else{
            mOutputText.setText(user.getEmail());
//            startActivity(new Intent(this, MapsActivity.class));
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

    private void signOutUser(View view){
        mAuth.signOut();
        //updateUI();

    }

    private void initViews() {
        mEmailLayout = findViewById(R.id.et_email);
        mPasswordLayout = findViewById(R.id.et_password);
        mBtnSignin = findViewById(R.id.btn_singin);
        mBtnRegisterUser = findViewById(R.id.btn_registeruser);
        mOutputText = findViewById(R.id.tv_output);
        mProgressBar = findViewById(R.id.progressbar);
        mBtnSignoutUser = findViewById(R.id.btn_singoutuser);
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
        } else if (password.length() < 6) {
            mPasswordLayout.setHint("Password length short. Minimum 6 characters required.");
            return true;
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

