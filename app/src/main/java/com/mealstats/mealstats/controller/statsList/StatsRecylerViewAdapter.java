package com.mealstats.mealstats.controller.statsList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mealstats.mealstats.R;
import com.mealstats.mealstats.controller.dummy.DummyStatsInfo;
import com.mealstats.mealstats.controller.foodList.FoodRetrievalFragment;
import com.mealstats.mealstats.services.BackendMessagesParser;
import com.mealstats.mealstats.util.Constants;

import java.util.List;

/**
 * Created by felipe on 25/09/16.
 */
public class StatsRecylerViewAdapter  extends RecyclerView.Adapter<StatsRecylerViewAdapter.ViewHolder> {

    private final List<DummyStatsInfo> mValues;

    public StatsRecylerViewAdapter(List<DummyStatsInfo> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_stats, parent, false);

        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DummyStatsInfo mealInfo = mValues.get(position);

        Integer statNameId = BackendMessagesParser.getStatNameId(mealInfo.statName);
        String statName = (statNameId == null ? mealInfo.statName :
                holder.context.getResources().getString(statNameId));


        String statValue = (mealInfo.statName.equals(Constants.NAME_MEAL_STAT_RESPONSE) ?
                holder.context.getResources().getString(BackendMessagesParser.getFoodName(mealInfo.statValue)) :
                mealInfo.statValue );

        holder.mStatsNameView.setText(statName);

        holder.mStatsValueView.setText(statValue); //TODO Uase backend parser
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mStatsNameView, mStatsValueView;
        public final Context context;

        public ViewHolder(View view, Context context) {
            super(view);
            mView = view;
            mStatsNameView = (TextView) view.findViewById(R.id.stat_name);
            mStatsValueView = (TextView) view.findViewById(R.id.stat_value);
            this.context = context;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStatsNameView.getText() + "'";
        }
    }
}
