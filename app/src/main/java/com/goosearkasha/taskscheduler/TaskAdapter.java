package com.goosearkasha.taskscheduler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    public static final String TAG = "TaskAdapter";

    private LayoutInflater inflater;
    private List<Task> tasks;
    private Context context;

    TaskAdapter(Context context, List<Task> tasks) {
        Log.d(TAG, "QQ from constructor");
        this.tasks = tasks;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "QQ from onCreateViewHolder");
        View view = inflater.inflate(R.layout.tasks_list_item, parent, false);
        return new TaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "QQ from onBindViewHolder");
        Task task = tasks.get(position);
        holder.taskTitle.setText(task.getTitle());
        if(!task.getIsOpen()) {
            holder.taskTitle.setTextColor(context.getResources().getColor(R.color.DimGrey));
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;

        ViewHolder(View view) {
            super(view);
            Log.d(TAG, "QQ from ViewHolder Constructor");
            taskTitle = (TextView) view.findViewById(R.id.taskTitle);


           view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionIndex = getAdapterPosition();
                    Task task = tasks.get(positionIndex);

                    Log.d("taskItem", "element nom " + positionIndex);
                    Intent intent = new Intent(context, TaskActivity.class);
                    intent.putExtra(Task.class.getSimpleName(), task);
                    context.startActivity(intent);
                }
            });
        }
    }
}
