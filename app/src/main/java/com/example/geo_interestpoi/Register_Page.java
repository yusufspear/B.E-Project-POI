package com.example.geo_interestpoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class Register_Page extends AppCompatActivity {


    private TextInputLayout FullName, Email, Password, Repassword, PhoneNumber;
    private RadioButton GenderM, GenderFe;
    private Button Register;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);


        initViews();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        Register.setOnClickListener(this::createUser);



    }

    private void initViews() {

        FullName = findViewById(R.id.et_Fullname);
        Email = findViewById(R.id.et_email);
        Password = findViewById(R.id.et_password);
        Repassword = findViewById(R.id.et_repassword);
        PhoneNumber = findViewById(R.id.et_phone);
        Register = findViewById(R.id.btn_registeruser);
        GenderM= findViewById(R.id.rdMale);
        GenderFe= findViewById(R.id.rdFemale);




    }

    // Read from the database

    private void createUser(View view) {


        if (!validateEmailAddress() | !validatePassword() | !validateName() | !validatePhoneNumber()) {
            // Email or Password not valid,
            return;
        }
        //Email and Password valid, create user here
        String Gender ;
        String fullName = FullName.getEditText().getText().toString().trim();
        String phoneNumber = PhoneNumber.getEditText().getText().toString().trim();
        String email = Email.getEditText().getText().toString().trim();
        String password = Password.getEditText().getText().toString().trim();
        String repassword = Repassword.getEditText().getText().toString().trim();
        if (GenderM.isChecked()){
            Gender = "Male";
        }else {
            Gender = "FeMale";

        }

        if (!password.equals(repassword)) {
            Toast.makeText(this, "Password NOT Match", Toast.LENGTH_LONG).show();
            Repassword.setError("Password Not Match!");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                               @Override
                                               public void onComplete(@NonNull Task<AuthResult> task) {
                                                   if (task.isSuccessful()) {
//                                private void writeNewUser(String mAuth.getUid(), String fullName, String email,String password,String phoneNumber String Gender) {
                                                       User information = new User(fullName, email, password, phoneNumber, Gender);

                                                       mDatabase.getReference("https://geo-interest-poi.firebaseio.com/Users").child(mAuth.getCurrentUser().getUid()).setValue(information);
                                                       Toast.makeText(Register_Page.this, "User Created Successfully\n Waiting For Authentication ", Toast.LENGTH_LONG).show();

                                                   } else {
                                                       Toast.makeText(Register_Page.this, "Error Occur", Toast.LENGTH_LONG).show();
                                                   }
                                               }
                                           }
                    )
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Register_Page.this, "Email Already in Database", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }


    private boolean validateEmailAddress() {

        String email = Email.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            Email.setError("Email is required. Can't be empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Invalid Email. Enter valid email address.");
            return false;
        } else {
            Email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        String password = Password.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            Password.setError("Password is required. Can't be empty.");
            return false;
        } else if (password.length() < 7) {
            Password.setError("Password is Week");
            return true;
        } else {
            Password.setError(null);
            return true;
        }

    }


    private boolean validateName() {

        String fullName = FullName.getEditText().getText().toString().trim();

        if (fullName.isEmpty()) {
            FullName.setError("Name Field Can't be empty!");
            return false;
        } else if (fullName.length() < 5) {
            FullName.setError("Name is Too Short!");
            return true;
        } else {
            FullName.setError(null);
            return true;
        }

    }

    private boolean validatePhoneNumber() {

        String phoneNumber = PhoneNumber.getEditText().getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            PhoneNumber.setError("PhoneNumber is required");
            return false;
        } else if (phoneNumber.length() < 10) {
            PhoneNumber.setError("10 Digit Required!");
            return true;
        } else {
            PhoneNumber.setError(null);
            return true;
        }

    }




}