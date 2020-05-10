package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddOrChangeRecord extends AppCompatActivity {

    final String TAG = "AddOrChangeActivity";

    public static final int ADD_RECORD = 0;
    public static final int UPDATE_RECORD= 1;

    int mode = ADD_RECORD;

    Button saveButton;
    Button backButton;
    EditText comment;
    Button addTimeButton;
    ActionBar actionbar;

    DBHelper dbHelper;

    Calendar dateAndTime=Calendar.getInstance();
    String time;

    Task task;
    TakeTimeRecord record = new TakeTimeRecord();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_change_record);

        comment = (EditText) findViewById(R.id.comment);
        addTimeButton = (Button) findViewById(R.id.addTimeButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        backButton = (Button) findViewById(R.id.backButton);

        setInitialDateTime();

        Bundle arguments = getIntent().getExtras();
        actionbar = getSupportActionBar();

        if(arguments!=null){
            mode = arguments.getInt("mode");

            if(mode == ADD_RECORD) {
                task = (Task) arguments.getSerializable(Task.class.getSimpleName());
                actionbar.setTitle("Добавление записи");

            }
            if(mode == UPDATE_RECORD){
                record = (TakeTimeRecord) arguments.getSerializable(TakeTimeRecord.class.getSimpleName());
                comment.setText(record.getComment());
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                time = dateFormat.format(record.getTime().getTime());
                Log.d(TAG, time);
                actionbar.setTitle("Изменение записи");
            }
        }

        dbHelper = new DBHelper(this);
    }

    public void saveRecord(View view) {

        String c = comment.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.COLUMN_COMMENT, c);
        newValues.put(DBHelper.COLUMN_TIME, time);

        Log.d(TAG, "mode = " + mode);


        if(mode == ADD_RECORD) {

            Log.d(TAG, "--- Insert in database: ---");
            Log.d(TAG,  "values: " + DBHelper.COLUMN_COMMENT + " = " + c + " " +
                    DBHelper.COLUMN_TIME + " = " + time);

            int taskID = task.getID();
            newValues.put(DBHelper.COLUMN_TASK_ID, taskID);

            db.insert(DBHelper.TABLE_TAKE_TIME, null, newValues);
        }
        if(mode == UPDATE_RECORD){
            Log.d(TAG,"QQ from update task");

            String where =  DBHelper.COLUMN_ID + " = " + record.getID();
            db.update(DBHelper.TABLE_TAKE_TIME, newValues, where, null);
        }

        Cursor cursor = db.query(DBHelper.TABLE_TAKE_TIME, null, null, null, null, null, null);
        Log.d(TAG, "getCount = " + cursor.getCount());
        cursor.close();
        dbHelper.close();

        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
        finish();
    }

    public void back(View view) {

        Intent intent = new Intent(this, TaskActivity.class);

        startActivity(intent);
        finish();
    }

    public void setTime(View v) {
        new TimePickerDialog(AddOrChangeRecord.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }


    // установка начальных даты и времени
    private void setInitialDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        time = dateFormat.format(dateAndTime.getTime());
        Log.d(TAG, time);
    }


    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };



}
