package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.ActionBar;
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
    Group group;
    String mode = "Добавление группы";

    Button saveButton;
    Button backButton;
    EditText title;
    EditText description;
    ActionBar actionbar;

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

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            group = (Group) arguments.getSerializable(Group.class.getSimpleName());
            mode = arguments.getString("mode");
            title.setText(group.getTitle());
            description.setText(group.getDescription());
        }

        actionbar = getSupportActionBar();
        actionbar.setTitle(mode);
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

        if(mode == "Добавление группы") {
            db.insert(DBHelper.TABLE_GROUPS, null, newValues);
        } else {
            String where =  DBHelper.COLUMN_ID + " = " + group.getID();
            db.update(DBHelper.TABLE_GROUPS, newValues, where, null);
        }
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
