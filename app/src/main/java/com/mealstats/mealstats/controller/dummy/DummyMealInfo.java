package com.mealstats.mealstats.controller.dummy;

public class DummyMealInfo {
    public final String id;
    public final String mealName;

    public DummyMealInfo(String id, String mealName) {
        this.id = id;
        this.mealName = mealName;
    }

    @Override
    public String toString() {
        return mealName;
    }
}
