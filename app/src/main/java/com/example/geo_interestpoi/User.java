package com.example.geo_interestpoi;

public class User {

    private String username;
    private String email;
    private String password;
    private String  phoneNumber;
    private String gender;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String password, String  phoneNumber, String gender) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

}