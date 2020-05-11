package com.goosearkasha.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    final String TAG = "GoalActivityLog";
    final int STATUS_START = 0; // загрузка данных начата
    final int STATUS_FINISH = 1; // загрузка завершена

    BarChart barChart;
    TextView legendTextView;
    private Handler h;
    private DBHelper dbHelper;

    Goal goal;
    ArrayList<BarEntry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        barChart = (BarChart) findViewById(R.id.barChart);
        legendTextView = (TextView) findViewById(R.id.legend);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            goal = (Goal) arguments.getSerializable(Goal.class.getSimpleName());
        }
        dbHelper = new DBHelper(this);

        entries = new ArrayList<>();
        BarDataSet dataset = new BarDataSet(entries, "# take time");
        Log.d(TAG, "listSize = " + entries.size());
        BarData data = new BarData(dataset);


        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case STATUS_START:
                        Log.d(TAG, "status = " + STATUS_START);
                        break;
                    case STATUS_FINISH:
                        BarDataSet dataset = new BarDataSet(entries, "# take time");
                        Log.d(TAG, "listSize = " + entries.size());
                        BarData data = new BarData(dataset);
                        barChart.setData(data);
                        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                        break;
                }
            }
        };

    }


    @Override
    protected void onResume() {
        super.onResume();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                h.sendEmptyMessage(STATUS_START);

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                Log.d(TAG, "goalID = " + goal.getID());
                String selection = DBHelper.COLUMN_GOAL_ID + " = ?";
                String[] selectionArgs = {Integer.toString(goal.getID())};
                Cursor cursor = database.query(DBHelper.TABLE_TASKS, null, selection, selectionArgs, null, null, null);
                Log.d(TAG, "getCount = " + cursor.getCount());
                String legend = "Ваши задачи:\n";

                if(cursor.moveToFirst()) {
                    int titleIndex = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
                    int timeIndex = cursor.getColumnIndex(DBHelper.COLUMN_TIME);

                    int i = 0;

                    do {
                        Log.d(TAG,"title = " + cursor.getString(titleIndex) +
                                ", time = " + cursor.getInt(timeIndex));
                        legend = legend + i + " - " + cursor.getString(titleIndex) + ": "
                                + cursor.getInt(timeIndex) + " минут\n";
                        entries.add(new BarEntry(i, cursor.getInt(timeIndex)));
                        i++;

                    }while (cursor.moveToNext());
                } else
                    Log.d(TAG, "0 rows");
                Log.d(TAG, "QQ");

                legendTextView.setText(legend);
                cursor.close();
                dbHelper.close();
                h.sendEmptyMessage(STATUS_FINISH);
            }
        });
        thread.start();
    }
}
