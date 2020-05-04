package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddOrChangeGoalActivity extends AppCompatActivity {

    final String TAG = "AddOrChangeActivity";

    Button saveButton;
    Button backButton;
    EditText title;
    EditText description;
    EditText deadline;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_change_goal);
    }
}
