package com.mealstats.mealstats.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mealstats.mealstats.R;
import com.mealstats.mealstats.model.User;
import com.mealstats.mealstats.util.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment  {

    private SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    public ConfigFragment() {
        // Required empty public constructor


    }

    public static ConfigFragment newInstance() {
        ConfigFragment fragment = new ConfigFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    public int getGender(){
        RadioGroup group = (RadioGroup) getView().findViewById(R.id.group_gender);
        int result = 0;
        if( group.getCheckedRadioButtonId() == R.id.radioButtonMale){
            result = Constants.MALE;
        }else if( group.getCheckedRadioButtonId() == R.id.radioButtonFemale ){
            result = Constants.FEMALE;
        }
        return result;
    }

    public int getActivityUser(){
        RadioGroup group = (RadioGroup) getView().findViewById(R.id.group_type);
        int result = 0;
        if( group.getCheckedRadioButtonId() == R.id.radioButtonLight){
            result = Constants.LIGHT;
        }else if( group.getCheckedRadioButtonId() == R.id.radioButtonModerate ){
            result = Constants.MODERATE;
        }else if( group.getCheckedRadioButtonId() == R.id.radioButtonActive){
            result = Constants.ACTIVE;
        }
        return result;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_config, container, false);

        Button save = (Button) rootView.findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputAge = (EditText) getView().findViewById(R.id.input_age);
                EditText inputWeight = (EditText) getView().findViewById(R.id.input_weight);
                EditText inputHeight = (EditText) getView().findViewById(R.id.input_height);

                try {
                    double age = Double.parseDouble(inputAge.getText().toString());
                    double weight = Double.parseDouble(inputWeight.getText().toString());
                    double height = Double.parseDouble(inputHeight.getText().toString());
                    int genre = getGender();
                    int activity = getActivityUser();

                    LoginActivity.currentUser.setAge(age);
                    LoginActivity.currentUser.setWeight(weight);
                    LoginActivity.currentUser.setHeight(height);
                    LoginActivity.currentUser.setActivityLevel(User.Activity.values()[activity]);
                    LoginActivity.currentUser.setGenre(User.Genre.values()[genre]);
                    LoginActivity.currentUser.findCalories();
                    LoginActivity.currentUser.setCurrentDayCalories(0);

                    Toast.makeText(getContext(), R.string.settings_updated, Toast.LENGTH_SHORT).show();

                    goToMainActivity();
                } catch (Exception e) {
                    Toast.makeText(getContext(), R.string.settings_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }


    public void goToMainActivity() {
        startActivity(new Intent(getContext(), MealStatsActivity.class));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
