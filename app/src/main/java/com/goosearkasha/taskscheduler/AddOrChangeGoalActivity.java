package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddOrChangeGoalActivity extends AppCompatActivity {

    final String TAG = "AddOrChangeActivity";

    Button saveButton;
    Button backButton;
    EditText title;
    EditText description;
    TextView GroupIDTextView;

    DBHelper dbHelper;

    Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_change_goal);

        description = (EditText) findViewById(R.id.description);
        title = (EditText) findViewById(R.id.title);
        GroupIDTextView = (TextView) findViewById(R.id.GroupID);
        saveButton = (Button) findViewById(R.id.saveButton);
        backButton = (Button) findViewById(R.id.backButton);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            group = (Group) arguments.getSerializable(Group.class.getSimpleName());
            GroupIDTextView.setText("Ваша цель будет прикреплена к группе '" + group.getTitle() + "'");
        }

        dbHelper = new DBHelper(this);
    }

    public void saveGoal(View view) {

        String t = title.getText().toString();
        String d = description.getText().toString();
        int grid=group.getID();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(TAG, "--- Insert in database: ---");
        Log.d(TAG, "title = " + t + " description = " + d + " groupID = " + grid);


        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.COLUMN_TITLE, t);
        newValues.put(DBHelper.COLUMN_DESCRIPTION, d);
        newValues.put(DBHelper.COLUMN_GOAL_ID, grid);
        newValues.put(DBHelper.COLUMN_DEADLINE, "01-01-2020");
        newValues.put(DBHelper.COLUMN_IS_OPEN, 1);
        db.insert(DBHelper.TABLE_GOALS, null, newValues);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_GOALS, null, null, null, null, null, null);
        Log.d(TAG, "getCount = " + cursor.getCount());
        dbHelper.close();

        Intent intent = new Intent(this, GroupActivity.class);
        startActivity(intent);
        finish();
    }

    public void back(View view) {
        Intent intent = new Intent(this, GroupActivity.class);
        startActivity(intent);
        finish();
    }
}
