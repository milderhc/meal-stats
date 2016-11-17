package com.mealstats.mealstats.controller;

import android.content.Context;
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

import com.mealstats.mealstats.R;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    public ConfigFragment() {
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2) {
        ConfigFragment fragment = new ConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    public String getGender(){
        RadioGroup group = (RadioGroup) getView().findViewById(R.id.group_gender);
        String result = "";
        if( group.getCheckedRadioButtonId() == R.id.radioButtonMale){
            result = Constants.MALE;
        }else if( group.getCheckedRadioButtonId() == R.id.radioButtonFemale ){
            result = Constants.FEMALE;
        }
        return result;
    }

    public String getActivityUser(){
        RadioGroup group = (RadioGroup) getView().findViewById(R.id.group_type);
        String result = "";
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
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                EditText inputAge = (EditText) getView().findViewById(R.id.input_age);
                EditText inputWeight = (EditText) getView().findViewById(R.id.input_weight);
                EditText inputHeight = (EditText) getView().findViewById(R.id.input_height);

                String age = inputAge.getText().toString();
                String weight = inputWeight.getText().toString();
                String height = inputHeight.getText().toString();
                String gender = getGender();
                String activity = getActivityUser();
                Log.e("Lol",inputAge.getText().toString());
                Log.e("Lol",inputWeight.getText().toString());
                Log.e("Lol",inputHeight.getText().toString());
                Log.e("Lol",gender.toString());
                Log.e("Lol",activity);

                editor.putString(Constants.GENDER,gender);
                editor.putString(Constants.AGE,age);
                editor.putString(Constants.WEIGHT,weight);
                editor.putString(Constants.HEIGHT,height);
                editor.putString(Constants.ACTIVITY,activity);
                editor.commit();


            }
        });

        return rootView;
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSave:
                SharedPreferences.Editor editor = sharedPreferences.edit();

                EditText inputAge = (EditText) getView().findViewById(R.id.input_age);
                EditText inputWeight = (EditText) getView().findViewById(R.id.input_weight);
                EditText inputHeight = (EditText) getView().findViewById(R.id.input_height);

                String age = inputAge.getText().toString();
                String weight = inputWeight.getText().toString();
                String height = inputHeight.getText().toString();
                Log.e("Lol",inputAge.getText().toString());
                Log.e("Lol",inputWeight.getText().toString());
                Log.e("Lol",inputHeight.getText().toString());


                //editor.putString(Constants.GENDER,getGender());
                editor.putString(Constants.AGE,age);
                editor.putString(Constants.WEIGHT,weight);
                editor.putString(Constants.HEIGHT,height);
                //editor.putString(Constants.ACTIVITY,getActivity());
                editor.commit();
                break;
        }
    }*/


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
