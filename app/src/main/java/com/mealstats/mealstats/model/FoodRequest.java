package com.mealstats.mealstats.model;

import com.mealstats.mealstats.controller.dummy.DummyMealInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FoodRequest implements Serializable{
    private Date date;
    Map<String, String> mealInfo;
    private String mealName;

    public FoodRequest(Map<String, String> mealInfo, String mealName) {
        this.mealInfo = mealInfo;
        this.date = new Date();
        this.mealName = mealName;
    }

    public Map<String, String> getMealsInfo(){
        return mealInfo;
    }


    public Date getDate(){ return date; }

    public String getMealName(){ return mealName; }

    public String toString(){
        return date +  "  " + mealName + " " + mealInfo;
    }
}
