package com.mealstats.mealstats.model;

import android.util.Log;

/**
 * Created by milder on 2/11/16.
 */
public class User {
    private String email;

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
