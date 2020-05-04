package com.goosearkasha.taskscheduler;

import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Goal implements Serializable {

    public static final String TAG = "GoalClass";

    private int ID;
    private String title;
    private String description;
    private Calendar deadline;
    private int groupID;
    private boolean isOpen;

    public Goal() {
        this.ID = 0;
        this.title = "Нет данных";
        this.description = "Нет данных";
        this.deadline = new GregorianCalendar(2000, Calendar.SEPTEMBER, 1);
        this.groupID = 0;
        this.isOpen = true;
    }

    public Goal(int ID, String title, String description, String deadline, int groupID, int isOpen) {
        Log.d(TAG, "QQ");
        this.ID = ID;
        this.title = title;
        this.description = description;

        if(deadline.length() == 10) {
            Log.d(TAG, "QQ from if");
            String dayString = deadline.substring(0, 2);
            int dayInt = Integer.parseInt(dayString);
            Log.d(TAG,"dayString = " + dayString);
            Log.d(TAG,"dayInt = " + dayInt);

            String monthString = deadline.substring(3, 5);
            int monthInt = Integer.parseInt(monthString);
            Log.d(TAG,"monthString = " + monthString);
            Log.d(TAG,"monthInt = " + monthInt);

            String yearString = deadline.substring(6, 10);
            int yearInt = Integer.parseInt(yearString);
            Log.d(TAG,"yearString = " + yearString);
            Log.d(TAG,"yearInt = " + yearInt);

            this.deadline = new GregorianCalendar(yearInt, monthInt, dayInt);
        } else {
            this.deadline = new GregorianCalendar(2000, Calendar.SEPTEMBER, 1);
        }

        this.groupID = groupID;

        if(isOpen == 0) {
            this.isOpen = false;
        } else {
            this.isOpen = true;
        }

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public boolean getOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }
}
