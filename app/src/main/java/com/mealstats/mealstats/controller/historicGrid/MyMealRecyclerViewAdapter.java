package com.mealstats.mealstats.controller.historicGrid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mealstats.mealstats.R;

import com.mealstats.mealstats.controller.historicGrid.dummy.DummyContent.DummyItem;
import com.mealstats.mealstats.services.BackendMessagesParser;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMealRecyclerViewAdapter extends RecyclerView.Adapter<MyMealRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final MealFragment.OnListFragmentInteractionListener mListener;

    public MyMealRecyclerViewAdapter(List<DummyItem> items, MealFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_meal, parent, false);
        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String mealName = mValues.get(position).content;
        holder.mContentView.setText(holder.context.getResources().getString(BackendMessagesParser.getFoodName(mealName)));
        holder.mImageView.setImageResource(BackendMessagesParser.getFoodIcon(mealName));
        holder.mDateView.setText(holder.mItem.getDate().toString());

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
        public final TextView mDateView;

        public Context context;
        public DummyItem mItem;

        public ViewHolder(View view, Context context) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.imageViewHistoric);
            mDateView = (TextView) view.findViewById(R.id.requestDate);
            this.context = context;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
