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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddOrChangeGoalActivity extends AppCompatActivity {

    final String TAG = "AddOrChangeActivity";

    public static final int ADD_GOAL = 0;
    public static final int UPDATE_GOAL = 1;

    int mode = ADD_GOAL;

    Button saveButton;
    Button backButton;
    EditText title;
    EditText description;
    TextView GroupIDTextView;
    ActionBar actionbar;
    TextView description1;

    DBHelper dbHelper;

    Group group ;
    Goal goal = new Goal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_change_goal);

        description = (EditText) findViewById(R.id.description);
        title = (EditText) findViewById(R.id.title);
        GroupIDTextView = (TextView) findViewById(R.id.GroupID);
        saveButton = (Button) findViewById(R.id.saveButton);
        backButton = (Button) findViewById(R.id.backButton);
        description1 = (TextView) findViewById(R.id.description1);

        Bundle arguments = getIntent().getExtras();
        actionbar = getSupportActionBar();

        if(arguments!=null){
            mode = arguments.getInt("mode");

            if(mode == ADD_GOAL) {
                group = (Group) arguments.getSerializable(Group.class.getSimpleName());
                GroupIDTextView.setText("Ваша цель прикреплена к группе '" + group.getTitle() + "'");
                actionbar.setTitle("Добавление цели");
                description1.setText(R.string.add_or_change_goal_description);

            }
            if(mode == UPDATE_GOAL){
                goal = (Goal) arguments.getSerializable(Goal.class.getSimpleName());
                title.setText(goal.getTitle());
                description.setText(goal.getDescription());
                GroupIDTextView.setVisibility(View.GONE);
                actionbar.setTitle("Изменение цели");
                description1.setText(R.string.change_goal_description);
            }
        }

        dbHelper = new DBHelper(this);
    }

    public void saveGoal(View view) {

        String t = title.getText().toString();
        String d = description.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(TAG, "--- Insert in database: ---");
        Log.d(TAG, "title = " + t + " description = ");


        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.COLUMN_TITLE, t);
        newValues.put(DBHelper.COLUMN_DESCRIPTION, d);


        if(mode == ADD_GOAL) {
            String grid=Integer.toString(group.getID());
            newValues.put(DBHelper.COLUMN_GROUP_ID, grid);
            newValues.put(DBHelper.COLUMN_DEADLINE, "01-01-2020");
            db.insert(DBHelper.TABLE_GOALS, null, newValues);
        } else {
            String where =  DBHelper.COLUMN_ID + " = " + goal.getID();
            db.update(DBHelper.TABLE_GOALS, newValues, where, null);
        }

        Cursor cursor = db.query(DBHelper.TABLE_GOALS, null, null, null, null, null, null);
        Log.d(TAG, "getCount = " + cursor.getCount());
        cursor.close();
        dbHelper.close();

        Intent intent = new Intent(this, GroupActivity.class);
        startActivity(intent);
        finish();
    }

    public void back(View view) {

        Intent intent = new Intent();

        if(mode == ADD_GOAL) {
            intent = new Intent(this, GroupActivity.class);
        }
        if(mode == UPDATE_GOAL) {
            intent = new Intent(this, GoalActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
