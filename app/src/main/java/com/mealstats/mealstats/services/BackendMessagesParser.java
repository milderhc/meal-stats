package com.mealstats.mealstats.services;

import com.mealstats.mealstats.R;

import java.util.HashMap;
import java.util.Map;
import android.util.Log;

//TODO: Add error messages
public class BackendMessagesParser {
    private static Map<String, Integer> foodIconsId, foodStringsId, statsNamesId;
    static {
        foodIconsId = new HashMap<>();
        foodIconsId.put("pizza", R.drawable.pizza);
        foodIconsId.put("hamburger", R.drawable.hamburguer);
        foodIconsId.put("hot dog", R.drawable.hotdog);
        foodIconsId.put("donuts", R.drawable.donut);
        foodIconsId.put("french fries", R.drawable.fries);
        foodIconsId.put("fried rice", R.drawable.rice);
        foodIconsId.put("spaghetti bolognese", R.drawable.spaguetti);
        foodIconsId.put("nachos", R.drawable.nachos);
        foodIconsId.put("club sandwich", R.drawable.sandwich);
        foodIconsId.put("ice cream", R.drawable.icecream);
        foodIconsId.put("default", R.drawable.default_icon);


        foodStringsId = new HashMap<>();
        foodStringsId.put("pizza", R.string.pizza);
        foodStringsId.put("hamburger", R.string.hamburger);
        foodStringsId.put("hot dog", R.string.hotdog);
        foodStringsId.put("donuts", R.string.donuts);
        foodStringsId.put("french fries", R.string.fries);
        foodStringsId.put("fried rice", R.string.rice);
        foodStringsId.put("spaghetti bolognese", R.string.spaghetti);
        foodStringsId.put("nachos", R.string.nachos);
        foodStringsId.put("club sandwich", R.string.sandwich);
        foodStringsId.put("ice cream", R.string.icecream);
        foodStringsId.put("default", R.string.unknownfood);

        statsNamesId = new HashMap<>();
        statsNamesId.put("name", R.string.mealname);
        statsNamesId.put("stats", R.string.fakestats);
    }

    //Returns the id of the food icon to show in the fragment
    public static int getFoodIcon(String name){
        if(foodIconsId.containsKey(name))
            return foodIconsId.get(name);
        else
            return foodIconsId.get("default");
    }

    //Returns the id of the Food Name to display in the app
    public static int getFoodName(String s){
        if(foodStringsId.containsKey(s))
            return foodStringsId.get(s);
        else
            return foodStringsId.get("default");
    }


    //Returns the id of the Food Name of the stat, eg. Calories, etc.
    public static Integer getStatNameId(String s){
        if(statsNamesId.containsKey(s))
            return statsNamesId.get(s);
        else
            return null;
    }

}
