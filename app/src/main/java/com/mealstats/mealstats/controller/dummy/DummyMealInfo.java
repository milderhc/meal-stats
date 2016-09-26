package com.mealstats.mealstats.controller.dummy;

import com.mealstats.mealstats.util.Constants;

import java.util.Map;

public class DummyMealInfo {
    public final String mealName;
    public final Map<String, String> stats;

    public DummyMealInfo(Map<String, String> stats) {
        this.stats = stats;
        this.mealName = stats.get(Constants.NAME_MEAL_STAT_RESPONSE);
    }

    @Override
    public String toString() {
        return mealName;
    }
}
