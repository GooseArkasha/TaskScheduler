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

public class GoalActivity extends AppCompatActivity {

    static Goal goal;
    final String TAG = "GoalActivityLog";
    final int STATUS_START = 0; // загрузка данных начата
    final int STATUS_FINISH = 1; // загрузка завершена

    private List<Task> tasks = new ArrayList<>();

    private TextView description;
    private Button backButton;
    private Button updateGoalButton;
    private Button removeGoalButton;
    private Button addButton;
    private RecyclerView recyclerView;
    ActionBar actionbar;

    private Handler h;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_activity);

        Log.d(TAG, "QQ from GoalActivity");

        description = (TextView) findViewById(R.id.goal_description);
        recyclerView = (RecyclerView) findViewById(R.id.tasks_recyclerView);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setText(R.string.add_task);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setText(R.string.back);

        updateGoalButton = (Button) findViewById(R.id.updateGoalButton);
        updateGoalButton.setText(R.string.update_goal);

        removeGoalButton = (Button) findViewById(R.id.removeGoalButton);
        removeGoalButton.setText(R.string.remove_goal);

        dbHelper = new DBHelper(this);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            goal = (Goal) arguments.getSerializable(Goal.class.getSimpleName());
            Log.d(TAG, "Goal received");
        }

        actionbar = getSupportActionBar();
        actionbar.setTitle("Цель: " + goal.getTitle());

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
                        Log.d(TAG, "title: " + goal.getTitle() + "description: " + goal.getDescription());

                        description.setText(goal.getDescription());
                        recyclerView.setVisibility(View.VISIBLE);
                        Log.d(TAG, "status = " + STATUS_FINISH);

                        Log.d(TAG, "size = " + tasks.size());

                        TaskAdapter taskAdapter = new TaskAdapter(GoalActivity.this, tasks);
                        recyclerView.setAdapter(taskAdapter);
                        break;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        tasks.clear();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                h.sendEmptyMessage(STATUS_START);

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                Log.d(TAG, "goalID = " + goal.getID());
                String selection = DBHelper.COLUMN_GOAL_ID + " = ?";
                String[] selectionArgs = {Integer.toString(goal.getID())};
                Cursor cursor = database.query(DBHelper.TABLE_TASKS, null, selection, selectionArgs, null, null, null);
                Log.d(TAG, "getCount = " + cursor.getCount());

                if(cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
                    int titleIndex = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
                    int descriptionIndex = cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION);
                    int deadlineIndex = cursor.getColumnIndex(DBHelper.COLUMN_DEADLINE);
                    int goalIDIndex = cursor.getColumnIndex(DBHelper.COLUMN_GOAL_ID);
                    int isOpenIndex = cursor.getColumnIndex(DBHelper.COLUMN_IS_OPEN);

                    do {
                        Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
                                ", title = " + cursor.getString(titleIndex) +
                                ", description = " + cursor.getString(descriptionIndex) +
                                ", deadline = " + cursor.getString(deadlineIndex) +
                                ", goalID = " + cursor.getInt(goalIDIndex) +
                                ", isOpen = " + cursor.getInt(isOpenIndex));
                        Task task = new Task(cursor.getInt(idIndex),
                                cursor.getString(titleIndex), cursor.getString(descriptionIndex),
                                cursor.getString(deadlineIndex), cursor.getInt(goalIDIndex),
                                cursor.getInt(isOpenIndex));
                        tasks.add(task);
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
        Intent intent = new Intent(this, AddOrChangeTaskActivity.class);
        intent.putExtra(Goal.class.getSimpleName(), goal);
        intent.putExtra("mode", AddOrChangeTaskActivity.ADD_TASK);
        startActivity(intent);
        finish();
    }

    public  void removeGoal(View view) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String whereForGoals =  DBHelper.COLUMN_ID + " = " + goal.getID();
        String whereForTasks =  DBHelper.COLUMN_GOAL_ID + " = " + goal.getID();

        database.delete(DBHelper.TABLE_GOALS, whereForGoals, null);
        database.delete(DBHelper.TABLE_TASKS, whereForTasks, null);

        Intent intent = new Intent(this, GroupActivity.class);
        startActivity(intent);
        finish();

    }

    public  void updateGoal(android.view.View view) {
        Intent intent = new Intent(this, AddOrChangeGoalActivity.class);
        intent.putExtra(Goal.class.getSimpleName(), goal);
        intent.putExtra("mode", AddOrChangeGoalActivity.UPDATE_GOAL);
        startActivity(intent);
        finish();
    }

    public void backToGroup(View view) {
        Intent intent = new Intent(this, GroupActivity.class);
        startActivity(intent);
        finish();
    }

    public void statistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra(Goal.class.getSimpleName(), goal);
        startActivity(intent);
        finish();
    }
}
