package com.mealstats.mealstats.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RadioGroup;

import com.mealstats.mealstats.R;
import com.mealstats.mealstats.util.Constants;

/**
 * Created by milder on 2/11/16.
 */
public class User {
    private String email;

    private double height, weight, age, requiredCalories;

    private double currentDayCalories;

    public static enum Activity {
        Light(0), Moderate(1), Active(2);

        private final int value;
        private Activity (int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public static enum Genre {
        Male(0), Female(1);

        private final int value;
        private Genre (int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private User.Activity activityLevel;
    private User.Genre genre;

    public User(String email) {
        this.email = email;
        currentDayCalories = 0;
    }

    public User(String email, double height, double weight, double age, double currentDayCalories, Activity activityLevel, Genre genre) {
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.currentDayCalories = currentDayCalories;
        this.activityLevel = activityLevel;
        this.genre = genre;
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

    public void saveUserData (Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat(Constants.AGE, (float)age);
        editor.putFloat(Constants.WEIGHT, (float)weight);
        editor.putFloat(Constants.HEIGHT, (float)height);
        editor.putFloat(Constants.CURRENT_DAY_CALORIES, (float)currentDayCalories);
        editor.putInt(Constants.ACTIVITY, activityLevel.getValue());
        editor.putInt(Constants.GENDER, genre.getValue());
        editor.commit();
    }

    public void loadUserData (Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        age = sharedPreferences.getFloat(Constants.AGE, 0);
        weight = sharedPreferences.getFloat(Constants.WEIGHT, 0);
        height = sharedPreferences.getFloat(Constants.HEIGHT, 0);
        currentDayCalories = sharedPreferences.getFloat(Constants.CURRENT_DAY_CALORIES, 0);
        activityLevel = Activity.values()[sharedPreferences.getInt(Constants.ACTIVITY, 0)];
        genre = Genre.values()[sharedPreferences.getInt(Constants.GENDER, 0)];

        findCalories();
    }

}
