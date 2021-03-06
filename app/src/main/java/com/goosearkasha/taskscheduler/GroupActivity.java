package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
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

    static Group group;
    final String TAG = "GroupActivityLog";
    final int STATUS_START = 0; // загрузка данных начата
    final int STATUS_FINISH = 1; // загрузка завершена

    private List<Goal> goals = new ArrayList<>();

    private TextView description;
    private Button backButton;
    private Button updateGroupButton;
    private Button removeGroupButton;
    private Button addButton;
    private RecyclerView recyclerView;
    ActionBar actionbar;

    private Handler h;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity);

        Log.d(TAG, "QQ fom GroupActivity");

        description = (TextView) findViewById(R.id.group_and_goal_description);
        recyclerView = (RecyclerView) findViewById(R.id.goals_and_tasks_recyclerView);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setText(R.string.add_goal);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setText(R.string.back);

        updateGroupButton = (Button) findViewById(R.id.updateGroupButton);
        updateGroupButton.setText(R.string.update_group);

        removeGroupButton = (Button) findViewById(R.id.removeGroupButton);
        removeGroupButton.setText(R.string.remove_group);

        dbHelper = new DBHelper(this);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            group = (Group) arguments.getSerializable(Group.class.getSimpleName());
        }

        actionbar = getSupportActionBar();
        actionbar.setTitle("Группа: " + group.getTitle());

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case STATUS_START:
                        addButton.setEnabled(false);
                        description.setText("Идет загрузка данных");
                        recyclerView.setVisibility(View.GONE);
                        Log.d(TAG, "status = " + STATUS_START);

                        if(group.getID() == 1) {
                            removeGroupButton.setVisibility(View.GONE);
                        }
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
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        goals.clear();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                h.sendEmptyMessage(STATUS_START);

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                Log.d(TAG, "groupID = " + group.getID());
                String selection = "group_id = ?";
                String[] selectionArgs = {Integer.toString(group.getID())};
                Cursor  cursor = database.query(DBHelper.TABLE_GOALS, null, selection, selectionArgs, null, null, null);
                Log.d(TAG, "getCount = " + cursor.getCount());

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
            }
        });
        thread.start();
    }


    public  void addItem(View view) {
        Intent intent = new Intent(this, AddOrChangeGoalActivity.class);
        intent.putExtra(Group.class.getSimpleName(), group);
        intent.putExtra("mode", AddOrChangeGoalActivity.ADD_GOAL);
        startActivity(intent);
        finish();
    }

    public  void removeGroup(View view) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues updatedGoalsValues = new ContentValues();
        updatedGoalsValues.put(DBHelper.COLUMN_GROUP_ID, 1);
        String where =  DBHelper.COLUMN_GROUP_ID + " = " + group.getID();
        database.update(DBHelper.TABLE_GOALS, updatedGoalsValues, where, null);

        database.delete(DBHelper.TABLE_GROUPS, DBHelper.COLUMN_ID + " = " + group.getID(), null);
        Intent intent = new Intent(this, GroupsActivity.class);
        startActivity(intent);
        finish();

    }

    public  void updateGroup(android.view.View view) {
        Intent intent = new Intent(this, AddOrChangeGroupActivity.class);
        intent.putExtra(Group.class.getSimpleName(), group);
        intent.putExtra("mode", "Изменение группы");
        startActivity(intent);
        finish();
    }

    public void backToGroups(View view) {
        Intent intent = new Intent(this, GroupsActivity.class);
        startActivity(intent);
        finish();
    }

}
