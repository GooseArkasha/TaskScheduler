package com.goosearkasha.taskscheduler;

import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TakeTimeRecord implements Serializable {

    public static final String TAG = "TakeTimeRecord";

    private int ID;
    private String comment;
    private Calendar time;
    private int taskID;

    public TakeTimeRecord() {
        this.ID = 0;
        this.comment = "Нет данных";
        this.time = new GregorianCalendar(2000, Calendar.SEPTEMBER, 1, 0, 0);
        this.taskID = 0;
    }

    public TakeTimeRecord(int ID, String comment, String time, int taskID) {

        this.ID = ID;
        this.comment = comment;
        Log.d(TAG, time + " len = " + time.length());

        if(time.length() == 5) {
            Log.d(TAG, "QQ from if");
            String hourString = time.substring(0, 2);
            int hourInt = Integer.parseInt(hourString);
            Log.d(TAG,"hourString = " + hourString);
            Log.d(TAG,"hourInt = " + hourInt);

            String minuteString = time.substring(3, 5);
            int minuteInt = Integer.parseInt(minuteString);
            Log.d(TAG,"minuteString = " + minuteString);
            Log.d(TAG,"minuteInt = " + minuteInt);

            this.time = new GregorianCalendar(2000, Calendar.SEPTEMBER, 1, hourInt, minuteInt);
        } else {
            this.time = new GregorianCalendar(2000, Calendar.SEPTEMBER, 1, 0, 0);
        }

        this.taskID = taskID;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
