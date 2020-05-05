package com.goosearkasha.taskscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    Group group;
    final String TAG = "GroupAndGoalActivityLog";
    final int STATUS_START = 0; // загрузка данных начата
    final int STATUS_FINISH = 1; // загрузка завершена

    private List<Goal> goals = new ArrayList<>();
    private boolean alreadyStarted;

    private TextView description;
    private Button addButton;
    private RecyclerView recyclerView;

    private Handler h;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_and_goal_activity);

        description = (TextView) findViewById(R.id.group_and_goal_description);
        addButton = (Button) findViewById(R.id.addButton);
        recyclerView = (RecyclerView) findViewById(R.id.goals_and_tasks_recyclerView);

        dbHelper = new DBHelper(this);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            group = (Group) arguments.getSerializable(Group.class.getSimpleName());
        }

        if(!alreadyStarted) {
            h = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case STATUS_START:
                            addButton.setEnabled(false);
                            description.setText("Идет загрузка данных");
                            recyclerView.setVisibility(View.GONE);
                            Log.d(TAG, "status = " + STATUS_START);
                            break;
                        case STATUS_FINISH:
                            addButton.setEnabled(true);
                            Log.d(TAG, "title: " + group.getTitle() + "description: " + group.getDescription());

                            description.setText(group.getDescription());
                            recyclerView.setVisibility(View.VISIBLE);
                            Log.d(TAG, "status = " + STATUS_FINISH);

                            Log.d(TAG, "size = " + goals.size());

                            GoalAdapter goalAdapter = new GoalAdapter(GroupActivity.this, goals);
                            recyclerView.setAdapter(goalAdapter);
                            break;
                    }
                };
            };
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!alreadyStarted) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    h.sendEmptyMessage(STATUS_START);

                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    Cursor cursor = database.query(DBHelper.TABLE_GOALS, null, null, null, null, null, null);
                    Log.d(TAG, "getcount = " + cursor.getCount());

                    if(cursor.moveToFirst()) {
                        int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
                        int titleIndex = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
                        int descriptionIndex = cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION);
                        int deadlineIndex = cursor.getColumnIndex(DBHelper.COLUMN_DEADLINE);
                        int groupIDIndex = cursor.getColumnIndex(DBHelper.COLUMN_GROUP_ID);
                        int isOpenIndex = cursor.getColumnIndex(DBHelper.COLUMN_IS_OPEN);

                        do {
                            Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
                                    ", title = " + cursor.getString(titleIndex) +
                                    ", description = " + cursor.getString(descriptionIndex) +
                                    ", deadline = " + cursor.getString(deadlineIndex) +
                                    ", groupID = " + cursor.getInt(groupIDIndex) +
                                    ", isOpen = " + cursor.getInt(isOpenIndex));
                            Goal goal = new Goal(cursor.getInt(idIndex),
                                    cursor.getString(titleIndex), cursor.getString(descriptionIndex),
                                    cursor.getString(deadlineIndex), cursor.getInt(groupIDIndex),
                                    cursor.getInt(isOpenIndex));
                            goals.add(goal);
                        }while (cursor.moveToNext());
                    } else
                        Log.d(TAG, "0 rows");
                    Log.d(TAG, "QQ");

                    cursor.close();
                    dbHelper.close();
                    h.sendEmptyMessage(STATUS_FINISH);
                    alreadyStarted = true;
                }
            });
            thread.start();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("threadStarted", true);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        alreadyStarted = savedInstanceState.getBoolean("threadStarted");
    }

    public  void addItem(View view) {
        Intent intent = new Intent(this, AddOrChangeGoalActivity.class);
        intent.putExtra(Group.class.getSimpleName(), group);
        startActivity(intent);
        finish();
    }
}
