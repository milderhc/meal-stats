package com.mealstats.mealstats.model;

import android.util.Log;

/**
 * Created by milder on 2/11/16.
 */
public class User {
    private String email;

    private int height, weight, age;

    public static enum Activity {
        Light, Moderate, Active
    }

    private User.Activity activityLevel;

    public User(String email, int height, int weight, int age, Activity activityLevel) {
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.activityLevel = activityLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Activity getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(Activity activityLevel) {
        this.activityLevel = activityLevel;
    }
}
