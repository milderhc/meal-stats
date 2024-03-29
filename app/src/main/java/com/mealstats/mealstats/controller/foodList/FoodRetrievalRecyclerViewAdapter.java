package com.mealstats.mealstats.controller.foodList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mealstats.mealstats.R;
import com.mealstats.mealstats.controller.foodList.FoodRetrievalFragment.OnListFragmentInteractionListener;
import com.mealstats.mealstats.controller.dummy.DummyMealInfo;
import com.mealstats.mealstats.services.BackendMessagesParser;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyMealInfo} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FoodRetrievalRecyclerViewAdapter extends RecyclerView.Adapter<FoodRetrievalRecyclerViewAdapter.ViewHolder> {

    private final List<DummyMealInfo> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FoodRetrievalRecyclerViewAdapter(List<DummyMealInfo> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_foodretrieval, parent, false);



        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String mealName = mValues.get(position).mealName;
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(holder.context.getResources().getString(BackendMessagesParser.getFoodName(mealName)));
        holder.mImageView.setImageResource(BackendMessagesParser.getFoodIcon(mealName));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public DummyMealInfo mItem;
        public final Context context;

        public ViewHolder(View view, Context context) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.imageViewFood);
            this.context = context;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
