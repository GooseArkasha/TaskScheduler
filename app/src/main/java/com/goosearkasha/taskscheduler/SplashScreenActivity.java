package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;


public class SplashScreenActivity extends AppCompatActivity {
    private final static String TAG = "SplashScreen";
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
                    Thread.sleep(1500); //Останавливаем работу потока на 2 с.
                } catch (Exception e) {
                    Log.d(TAG, "Sleep Eror");
                }

                Intent intent = new Intent(SplashScreenActivity.this, GroupsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        thread.start();
    }
}
