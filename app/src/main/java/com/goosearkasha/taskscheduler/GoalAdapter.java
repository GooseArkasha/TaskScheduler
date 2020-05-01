package com.goosearkasha.taskscheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Calendar;
import java.util.List;
import android.util.Log;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Goal> goals;
    private Context context;

    GoalAdapter(Context context, List<Goal> goals) {
        this.goals = goals;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.goals_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.goalTitle.setText(goal.getTitle());
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView goalTitle;

        ViewHolder(View view) {
            super(view);
            goalTitle = (TextView) view.findViewById(R.id.goalTitle);
        }
    }
}
