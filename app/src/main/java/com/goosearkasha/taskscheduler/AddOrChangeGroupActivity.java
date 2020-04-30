package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import 	android.content.ContentValues;
import android.util.Log;

public class AddOrChangeGroupActivity extends AppCompatActivity {

    final String TAG = "AddOrChangeActivity";

    Button saveButton;
    Button backButton;
    EditText title;
    EditText description;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_change_group);
        description = (EditText) findViewById(R.id.description);
        title = (EditText) findViewById(R.id.title);
        saveButton = (Button) findViewById(R.id.saveButton);
        backButton = (Button) findViewById(R.id.backButton);

        dbHelper = new DBHelper(this);
    }

    public void saveGroup(View view) {

        String t = title.getText().toString();
        String d = description.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(TAG, "--- Insert in database: ---");
        Log.d(TAG, "title = " + t + "description = " + d);


        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.COLUMN_TITLE, t);
        newValues.put(DBHelper.COLUMN_DESCRIPTION, d);
        db.insert(DBHelper.TABLE_GROUPS, null, newValues);
        dbHelper.close();

        Intent intent = new Intent(this, GroupsActivity.class);
        startActivity(intent);
        finish();
    }

    public void back(View view) {
        Intent intent = new Intent(this, GroupsActivity.class);
        startActivity(intent);
        finish();
    }
}
