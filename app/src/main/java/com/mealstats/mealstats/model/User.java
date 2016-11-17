package com.mealstats.mealstats.model;

import android.util.Log;

/**
 * Created by milder on 2/11/16.
 */
public class User {
    private String email;

    private double height, weight, age, requiredCalories;

    private double currentDayCalories;

    public static enum Activity {
        Light, Moderate, Active
    }
    public static enum Genre {
        Male, Female
    }

    private User.Activity activityLevel;
    private User.Genre genre;

    public User(String email) {
        this.email = email;
        currentDayCalories = 0;
    }

    public User(String email, double height, double weight, double age, double requiredCalories, Activity activityLevel, Genre genre) {
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.requiredCalories = requiredCalories;
        this.activityLevel = activityLevel;
        this.genre = genre;
        currentDayCalories = 0;
        findCalories();
    }

    public double findCalories () {
        double metabolicRate;
        if ( genre == Genre.Male )
            metabolicRate = 66.5 + (13.7 * weight) + (5 * height) - (6.8 * age);
        else
            metabolicRate = 655 + (9.6 * weight) + (1.85 * height) - (4.7 * age);

        double factor = 1;
        switch (activityLevel) {
            case Light:
                factor = genre == Genre.Male ? 1.55 : 1.56;
                break;
            case Moderate:
                factor = genre == Genre.Male ? 1.78 : 1.64;
                break;
            case Active:
                factor = genre == Genre.Male ? 2.10 : 1.82;
                break;
        }

        return requiredCalories = factor * metabolicRate;
    }

    public void addCurrentDayCalories (double cal) {
        currentDayCalories += cal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getRequiredCalories() {
        return requiredCalories;
    }

    public void setRequiredCalories(double requiredCalories) {
        this.requiredCalories = requiredCalories;
    }

    public Activity getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(Activity activityLevel) {
        this.activityLevel = activityLevel;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public double getCurrentDayCalories() {
        return currentDayCalories;
    }

    public void setCurrentDayCalories(double currentDayCalories) {
        this.currentDayCalories = currentDayCalories;
    }
}
