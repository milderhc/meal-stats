package com.mealstats.mealstats.controller.statsList;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mealstats.mealstats.R;
import com.mealstats.mealstats.controller.dummy.DummyMealInfo;
import com.mealstats.mealstats.controller.dummy.DummyStatsInfo;
import com.mealstats.mealstats.controller.foodList.FoodRetrievalRecyclerViewAdapter;
import com.mealstats.mealstats.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by felipe on 25/09/16.
 */
public class StatsFragment extends Fragment {

    private static String MEAL_INFO_BUNDLE = "MEAL_INFO_BUNDLE";
    private List<DummyStatsInfo> mealInfo;

    public StatsFragment(){}

    public static StatsFragment newInstance(Map<String, String> rawInfo){
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        HashMap<String, String> serializableRawInfo = new HashMap<>(rawInfo);
        args.putSerializable(MEAL_INFO_BUNDLE, serializableRawInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mealInfo = new ArrayList<>();
            HashMap<String, String> serializableRawInfo = (HashMap<String, String>) getArguments().getSerializable(MEAL_INFO_BUNDLE);
            for(Map.Entry<String, String> entry : serializableRawInfo.entrySet()){
                mealInfo.add(new DummyStatsInfo(entry.getKey(), entry.getValue()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new StatsRecylerViewAdapter(mealInfo));
        }

        return view;
    }
}
