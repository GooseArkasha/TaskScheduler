package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.ActionBar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    static Task task;
    final String TAG = "TaskActivityLog";
    final int STATUS_START = 0; // загрузка данных начата
    final int STATUS_FINISH = 1; // загрузка завершена

    private List<TakeTimeRecord> takeTimeRecords = new ArrayList<>();

    private TextView description;
    private TextView deadlineTextView;
    private Button backButton;
    private Button updateTaskButton;
    private Button removeTaskButton;
    private Button addButton;
    private RecyclerView recyclerView;
    ActionBar actionbar;

    private Handler h;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        Log.d(TAG, "QQ from TaskActivity");

        description = (TextView) findViewById(R.id.task_description);
        recyclerView = (RecyclerView) findViewById(R.id.record_recyclerView);

        deadlineTextView = (TextView) findViewById(R.id.deadlineTextView);


        addButton = (Button) findViewById(R.id.addButton);
        addButton.setText(R.string.add_record);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setText(R.string.back);

        updateTaskButton = (Button) findViewById(R.id.updateTaskButton);
        updateTaskButton.setText(R.string.update_task);

        removeTaskButton = (Button) findViewById(R.id.removeTaskButton);
        removeTaskButton.setText(R.string.remove_task);

        dbHelper = new DBHelper(this);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            task = (Task) arguments.getSerializable(Task.class.getSimpleName());
            Log.d(TAG, "Task received");
        }

        actionbar = getSupportActionBar();
        actionbar.setTitle("Задача: " + task.getTitle());

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case STATUS_START:
                        addButton.setEnabled(false);
                        description.setText("Идет загрузка данных");
                        recyclerView.setVisibility(View.GONE);
                        Log.d(TAG, "status = " + STATUS_START);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        deadlineTextView.setText("Дедлайн: " + dateFormat.format(task.getDeadline().getTime()));
                        break;
                    case STATUS_FINISH:
                        addButton.setEnabled(true);
                        Log.d(TAG, "title: " + task.getTitle() + "description: " + task.getDescription());

                        description.setText(task.getDescription());
                        recyclerView.setVisibility(View.VISIBLE);
                        Log.d(TAG, "status = " + STATUS_FINISH);

                        Log.d(TAG, "size = " + takeTimeRecords.size());

                        //TakeTimeRecordAdapter recordAdapter = new TakeTimeRecordAdapter(TaskActivity.this, takeTimeRecords);
                        //recyclerView.setAdapter(recordAdapter);
                        break;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        takeTimeRecords.clear();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                h.sendEmptyMessage(STATUS_START);

//                SQLiteDatabase database = dbHelper.getWritableDatabase();
//
//                Log.d(TAG, "goalID = " + goal.getID());
//                String selection = DBHelper.COLUMN_GOAL_ID + " = ?";
//                String[] selectionArgs = {Integer.toString(goal.getID())};
//                Cursor cursor = database.query(DBHelper.TABLE_TASKS, null, selection, selectionArgs, null, null, null);
//                Log.d(TAG, "getCount = " + cursor.getCount());
//
//                if(cursor.moveToFirst()) {
//                    int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
//                    int titleIndex = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
//                    int descriptionIndex = cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION);
//                    int deadlineIndex = cursor.getColumnIndex(DBHelper.COLUMN_DEADLINE);
//                    int goalIDIndex = cursor.getColumnIndex(DBHelper.COLUMN_GOAL_ID);
//                    int isOpenIndex = cursor.getColumnIndex(DBHelper.COLUMN_IS_OPEN);
//
//                    do {
//                        Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
//                                ", title = " + cursor.getString(titleIndex) +
//                                ", description = " + cursor.getString(descriptionIndex) +
//                                ", deadline = " + cursor.getString(deadlineIndex) +
//                                ", goalID = " + cursor.getInt(goalIDIndex) +
//                                ", isOpen = " + cursor.getInt(isOpenIndex));
//                        Task task = new Task(cursor.getInt(idIndex),
//                                cursor.getString(titleIndex), cursor.getString(descriptionIndex),
//                                cursor.getString(deadlineIndex), cursor.getInt(goalIDIndex),
//                                cursor.getInt(isOpenIndex));
//                        tasks.add(task);
//                    }while (cursor.moveToNext());
//                } else
//                    Log.d(TAG, "0 rows");
//                Log.d(TAG, "QQ");
//
//                cursor.close();
//                dbHelper.close();
                h.sendEmptyMessage(STATUS_FINISH);
            }
        });
        thread.start();
    }


    public  void addItem(View view) {
//        Intent intent = new Intent(this, AddOrChangeTaskActivity.class);
//        intent.putExtra(Goal.class.getSimpleName(), goal);
//        intent.putExtra("mode", AddOrChangeTaskActivity.ADD_TASK);
//        startActivity(intent);
//        finish();
    }

    public  void removeTask(View view) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String whereForTask =  DBHelper.COLUMN_ID + " = " + task.getID();
        String whereForRecord =  DBHelper.COLUMN_TASK_ID + " = " + task.getID();

        database.delete(DBHelper.TABLE_TASKS, whereForTask, null);
        database.delete(DBHelper.TABLE_TAKE_TIME, whereForRecord, null);

        Intent intent = new Intent(this, GoalActivity.class);
        startActivity(intent);
        finish();

    }

    public  void updateTask(android.view.View view) {
        Intent intent = new Intent(this, AddOrChangeTaskActivity.class);
        intent.putExtra(Task.class.getSimpleName(), task);
        intent.putExtra("mode", AddOrChangeTaskActivity.UPDATE_TASK);
        startActivity(intent);
        finish();
    }

    public void backToGoal(View view) {
        Intent intent = new Intent(this, GoalActivity.class);
        startActivity(intent);
        finish();
    }
}
