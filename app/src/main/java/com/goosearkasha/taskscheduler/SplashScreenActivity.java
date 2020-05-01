package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;


public class SplashScreenActivity extends AppCompatActivity {
    private final static String TAG = "SplashScreenTag";
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        dbHelper = new DBHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread thread = new Thread(new Runnable() { //Создаем новый поток
            @Override
            public void run() { //Переопределяем метод, который вызывается при запуске потока
                try {
                    Log.d(TAG, "SLEEEEEEEEP");
                    Thread.sleep(2000); //Останавливаем работу потока на 2 с.
                } catch (Exception e) {
                    Log.d(TAG, "Sleep Eror");
                }

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
                    }while (cursor.moveToNext());
                } else
                    Log.d(TAG, "0 rows");

                cursor.close();
                dbHelper.close();

                Intent intent = new Intent(SplashScreenActivity.this, GroupsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        thread.start();
    }
}
