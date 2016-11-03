package com.mealstats.mealstats.controller.foodList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mealstats.mealstats.R;
import com.mealstats.mealstats.controller.dummy.DummyMealInfo;
import com.mealstats.mealstats.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FoodRetrievalFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<DummyMealInfo> mealsNames = new ArrayList<>();
    private ArrayList<Map<String, String>> allNutritionalInfo = new ArrayList<>();

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    private OnListFragmentInteractionListener mListener;

    public static FoodRetrievalFragment newInstance(List<Map<String, String>> nutritionalInfo) {
        return newInstance(1, nutritionalInfo);
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FoodRetrievalFragment newInstance(int columnCount,
                                                    List<Map<String, String>> nutritionalInfo) {

        FoodRetrievalFragment fragment = new FoodRetrievalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        ArrayList<Map<String, String>> nutritionalInfoSerializable = new ArrayList<>(nutritionalInfo); //In order to be serializable
        args.putSerializable(Constants.NUTRITIONAL_INFO_ARGS, nutritionalInfoSerializable);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FoodRetrievalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            allNutritionalInfo = (ArrayList<Map<String, String>>) getArguments().getSerializable(Constants.NUTRITIONAL_INFO_ARGS);
            mealsNames = new ArrayList<>();
            for(int i=0; i<allNutritionalInfo.size(); i++){
                Map<String, String> mealStat = allNutritionalInfo.get(i);
                Log.d("Nutrition",mealStat.toString());
                mealsNames.add(new DummyMealInfo(mealStat));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodretrieval_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new FoodRetrievalRecyclerViewAdapter(mealsNames, mListener));
        }

        Log.d("fragment", "On create view");

        //imageView = (ImageView) view.findViewById(R.id.test);
        //imageView.setVisibility(View.VISIBLE);
        //Bitmap bitmap = BitmapFactory.
        //        decodeFile("/storage/emulated/0/Android/data/com.mealstats.mealstats/cache/img/img_to_analyze.jpg");
        //imageView.setImageBitmap(bitmap);



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyMealInfo item);
    }
}
