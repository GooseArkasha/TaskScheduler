package com.goosearkasha.taskscheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import android.util.Log;

class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Group> groups;
    private Context context;

    GroupAdapter(Context context, List<Group> groups) {
        this.groups = groups;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.groups_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.titleView.setText(group.getTitle());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView titleView;

        ViewHolder(View view){
            super(view);
            titleView = (TextView) view.findViewById(R.id.groupTitle);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionIndex = getAdapterPosition();
                    Group group = groups.get(positionIndex);

                    Log.d("groupItem", "element nom " + positionIndex);
                    Intent intent = new Intent(context, GroupActivity.class);
                    intent.putExtra(Group.class.getSimpleName(), group);
                    context.startActivity(intent);
                }
            });
        }
    }
}
