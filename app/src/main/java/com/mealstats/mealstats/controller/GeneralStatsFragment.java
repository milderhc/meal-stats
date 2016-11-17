package com.mealstats.mealstats.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mealstats.mealstats.R;
import com.mealstats.mealstats.controller.dummy.DummyStatsInfo;
import com.mealstats.mealstats.controller.statsList.StatsFragment;
import com.mealstats.mealstats.controller.statsList.StatsRecylerViewAdapter;
import com.mealstats.mealstats.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeneralStatsFragment extends Fragment {

    private static String MEAL_INFO_BUNDLE = "MEAL_INFO_BUNDLE";
    private List<DummyStatsInfo> mealInfo;

    private HashMap<String, String> info;

    public GeneralStatsFragment(){}

    public static GeneralStatsFragment newInstance(Map<String, String> rawInfo){
        GeneralStatsFragment fragment = new GeneralStatsFragment();
        Bundle args = new Bundle();
        HashMap<String, String> info = new HashMap<>(rawInfo);
        args.putSerializable(MEAL_INFO_BUNDLE, info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mealInfo = new ArrayList<>();
            info = (HashMap<String, String>) getArguments().getSerializable(MEAL_INFO_BUNDLE);

            loadFragment(StatsFragment.newInstance(info));
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
                User currentUser = LoginActivity.currentUser;

                double calories = Double.parseDouble(info.get(getString(R.string.energy)).split(" ")[0]);
                currentUser.addCurrentDayCalories(calories);

                double currentDayCalories = currentUser.getCurrentDayCalories();
                double requiredCalories = currentUser.getRequiredCalories();
                double percentage = currentDayCalories / requiredCalories;

                String message = "";
                if ( percentage >= 1.05 ) {
                    message = getString(R.string.calories_exceeded) +
                            "\nConsumed: " + (int)currentDayCalories +
                            "\nRecommended: " + (int)requiredCalories;
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else if ( percentage >= 0.9 ) {
                    message = getString(R.string.calories_close_to_limit) +
                            "\nConsumed: " + (int)currentDayCalories +
                            "\nRecommended: " + (int)requiredCalories;
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    message = getString(R.string.meal_added_to_diet);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
