package com.mealstats.mealstats.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mealstats.mealstats.R;
import com.mealstats.mealstats.controller.dummy.DummyStatsInfo;
import com.mealstats.mealstats.controller.statsList.StatsFragment;
import com.mealstats.mealstats.controller.statsList.StatsRecylerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralStatsFragment extends Fragment {

    private static String MEAL_INFO_BUNDLE = "MEAL_INFO_BUNDLE";
    private List<DummyStatsInfo> mealInfo;

    public GeneralStatsFragment(){}

    public static GeneralStatsFragment newInstance(Map<String, String> rawInfo){
        GeneralStatsFragment fragment = new GeneralStatsFragment();
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

            loadFragment(StatsFragment.newInstance(serializableRawInfo));
        }
    }

    private void loadFragment ( Fragment newFragment ) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.general_stats_content, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_stats, container, false);

        Button save = (Button) view.findViewById(R.id.eating_this_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
