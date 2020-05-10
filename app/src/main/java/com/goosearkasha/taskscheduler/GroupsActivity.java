package com.goosearkasha.taskscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {
    final String TAG = "GroupsActivityLog";

    final int STATUS_START = 0; // загрузка данных начата
    final int STATUS_FINISH = 1; // загрузка завершена

    List <Group> groups = new ArrayList<>();

    Handler h;
    TextView description;
    Button btnAdd;
    RecyclerView recyclerView;

    DBHelper dbHelper;
    ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_activity);

        description = (TextView) findViewById(R.id.groupsListDescription);
        btnAdd = (Button) findViewById(R.id.groupsListAddButton);
        recyclerView = (RecyclerView) findViewById(R.id.groupsListRecyclerView);
        dbHelper = new DBHelper(this);

        actionbar = getSupportActionBar();
        actionbar.setTitle("Ваши группы");


        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case STATUS_START:
                        btnAdd.setEnabled(false);
                        description.setText("Идет загрузка данных");
                        recyclerView.setVisibility(View.GONE);
                        Log.d(TAG, "status = " + STATUS_START);
                        break;
                    case STATUS_FINISH:
                        btnAdd.setEnabled(true);
                        description.setText("Это ваши группы." +
                                "Вы можете перейти к греппе, если нажмете на нее");
                        recyclerView.setVisibility(View.VISIBLE);
                        Log.d(TAG, "status = " + STATUS_FINISH);

                        GroupAdapter groupAdapter = new GroupAdapter(GroupsActivity.this, groups);
                        recyclerView.setAdapter(groupAdapter);
                        break;
                }
            };
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        groups.clear();

        Thread thread = new Thread(new Runnable() { //Создаем новый поток
            @Override
            public void run() { //Переопределяем метод, который вызывается при запуске потока

                h.sendEmptyMessage(STATUS_START);

                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = database.query(DBHelper.TABLE_GROUPS, null, null, null, null, null, null);

                if(cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
                    int titleIndex = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
                    int description = cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION);

                    do {
                        Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
                                ", title = " + cursor.getString(titleIndex) +
                                ", description = " + cursor.getString(description));
                        Group group = new Group(cursor.getInt(idIndex),
                                cursor.getString(titleIndex), cursor.getString(description));
                        groups.add(group);
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
        Intent intent = new Intent(this, AddOrChangeGroupActivity.class);
        startActivity(intent);
        finish();
    }

}
