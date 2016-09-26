package com.mealstats.mealstats.controller.dummy;


import java.io.Serializable;

/**
 * Created by felipe on 25/09/16.
 */
public class DummyStatsInfo implements Serializable {
    public final String statName, statValue;

    public DummyStatsInfo(String statName, String statValue) {
        this.statName = statName;
        this.statValue = statValue;
    }
}
