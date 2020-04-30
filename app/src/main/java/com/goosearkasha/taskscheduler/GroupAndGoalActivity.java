package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GroupAndGoalActivity extends AppCompatActivity {

    Group group;
    final String TAG = "GroupAndGoalActivityLog";

    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        description = (TextView) findViewById(R.id.group_and_goal_description);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            group = (Group) arguments.getSerializable(Group.class.getSimpleName());

            Log.d(TAG, "title: " + group.getTitle() + "description: " + group.getDescription());

            String string = "title: " + group.getTitle() + " description: " + group.getDescription();

            description.setText(string);
        }
        setContentView(R.layout.group_and_goal_page);
    }
}
