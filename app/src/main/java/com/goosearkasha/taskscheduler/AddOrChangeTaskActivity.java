package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.DatePickerDialog;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddOrChangeTaskActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    final String TAG = "AddOrChangeActivity";

    public static final int ADD_TASK = 0;
    public static final int UPDATE_TASK = 1;

    int mode = ADD_TASK;

    Button saveButton;
    Button backButton;
    EditText title;
    EditText description;
    TextView GoalIDTextView;
    ActionBar actionbar;
    TextView currentDateTime;
    Switch switchIsOpen;

    DBHelper dbHelper;

    Calendar dateAndTime=Calendar.getInstance();
    String deadline;

    Goal goal ;
    Task task = new Task();
    int isOpen = 1;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_change_task);

        currentDateTime = (TextView) findViewById(R.id.currentDateTime);
        description = (EditText) findViewById(R.id.description);
        title = (EditText) findViewById(R.id.title);
        GoalIDTextView = (TextView) findViewById(R.id.GoalID);
        saveButton = (Button) findViewById(R.id.saveButton);
        backButton = (Button) findViewById(R.id.backButton);
        switchIsOpen = (Switch) findViewById(R.id.switchIsOpen);
        if (switchIsOpen != null) {
            switchIsOpen.setOnCheckedChangeListener(this);
        }

        setInitialDateTime();

        Bundle arguments = getIntent().getExtras();
        actionbar = getSupportActionBar();

        if(arguments!=null){
            mode = arguments.getInt("mode");

            if(mode == ADD_TASK) {
                goal = (Goal) arguments.getSerializable(Goal.class.getSimpleName());
                GoalIDTextView.setText("Ваша задача прикреплена к цели '" + goal.getTitle() + "'");
                actionbar.setTitle("Добавление цели");

            }
            if(mode == UPDATE_TASK){
                task = (Task) arguments.getSerializable(Task.class.getSimpleName());
                title.setText(task.getTitle());
                description.setText(task.getDescription());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                deadline = dateFormat.format(task.getDeadline().getTime());
                currentDateTime.setVisibility(View.GONE);
                if(task.getIsOpen()) {
                    switchIsOpen.setChecked(false);
                } else {
                    switchIsOpen.setChecked(true);
                }
                GoalIDTextView.setVisibility(View.GONE);
                actionbar.setTitle("Изменение цели");
            }
        }

        dbHelper = new DBHelper(this);
    }

    public void saveTask(View view) {

        String t = title.getText().toString();
        String d = description.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.COLUMN_TITLE, t);
        newValues.put(DBHelper.COLUMN_DESCRIPTION, d);
        newValues.put(DBHelper.COLUMN_DEADLINE, deadline);
        newValues.put(DBHelper.COLUMN_IS_OPEN, isOpen);

        Log.d(TAG, "mode = " + mode);


        if(mode == ADD_TASK) {
            String goalId=Integer.toString(goal.getID());

            Log.d(TAG, "--- Insert in database: ---");
            Log.d(TAG,  "values: " + DBHelper.COLUMN_TITLE + " = " + t + " " +
                    DBHelper.COLUMN_DESCRIPTION + " = " + d + " " +
                    DBHelper.COLUMN_DEADLINE + " = " + deadline + " " +
                    DBHelper.COLUMN_GOAL_ID + " = " + goalId + " " +
                    DBHelper.COLUMN_IS_OPEN + " = " + isOpen);

            newValues.put(DBHelper.COLUMN_GOAL_ID, goalId);
            db.insert(DBHelper.TABLE_TASKS, null, newValues);
        }
        if(mode == UPDATE_TASK){
            Log.d(TAG,"QQ from update task");

            String where =  DBHelper.COLUMN_ID + " = " + task.getID();
            db.update(DBHelper.TABLE_TASKS, newValues, where, null);
        }

        Cursor cursor = db.query(DBHelper.TABLE_TASKS, null, null, null, null, null, null);
        Log.d(TAG, "getCount = " + cursor.getCount());
        cursor.close();
        dbHelper.close();

        Intent intent = new Intent(this, GoalActivity.class);
        startActivity(intent);
        finish();
    }

    public void back(View view) {

        Intent intent = new Intent();

        if(mode == ADD_TASK) {
            intent = new Intent(this, GoalActivity.class);
        }
        if(mode == UPDATE_TASK) {
            intent = new Intent(this, TaskActivity.class);
        }

        startActivity(intent);
        finish();
    }

    // отображаем диалоговое окно для выбора даты
    public void addDeadlineTask(View v) {
        new DatePickerDialog(AddOrChangeTaskActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    // установка начальных даты и времени
    private void setInitialDateTime() {

        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        deadline = dateFormat.format(dateAndTime.getTime());
        Log.d(TAG, deadline);
    }


    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                isOpen = 0;
            } else {
                isOpen = 1;
            }
            Log.d(TAG, Integer.toString(isOpen));
    }
}
