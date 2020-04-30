package com.goosearkasha.taskscheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import 	android.content.ContentValues;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "taskSchedulerDB";
    public static final String TABLE_GROUPS = "groups";
    public static final String TABLE_GOALS = "goals";
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_DAYS = "days";
    public static final String TABLE_TAKE_TIME = "take_time";
    public static final String TABLE_DAY_TASKS = "day_tasks";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DEADLINE = "deadline";
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_IS_OPEN = "is_open";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_GOAL_ID = "goal_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_DAY_ID = "day_id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_GROUPS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT default 'Тут может быть ваше описание'" +
                ");");

        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.COLUMN_TITLE, "Стандартные");
        db.insert(DBHelper.TABLE_GROUPS, null, newValues);

        db.execSQL("CREATE TABLE " + TABLE_GOALS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT default 'Назовите свою цель', " +
                COLUMN_DESCRIPTION + " TEXT default 'Тут может быть ваше описание', " +
                COLUMN_DEADLINE + " TEXT, " +
                COLUMN_GROUP_ID + " INTEGER default 1, " +
                COLUMN_IS_OPEN + " INTEGER default 1 " + //1 - открыта, 0 - закрыта
                ");");

        db.execSQL("CREATE TABLE " + TABLE_TASKS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT default 'Назовите свою задачу', " +
                COLUMN_DESCRIPTION + " TEXT default 'Тут может быть ваше описание', " +
                COLUMN_DEADLINE + " TEXT, " +
                COLUMN_GOAL_ID + " INTEGER, " +
                COLUMN_IS_OPEN + " INTEGER default 1 " + //1 - открыта, 0 - закрыта
                ");");

        db.execSQL("CREATE TABLE " + TABLE_DAYS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT " +
                ");");

        db.execSQL("CREATE TABLE " + TABLE_DAY_TASKS +
                " (" + COLUMN_TASK_ID + " INTEGER, " +
                COLUMN_DAY_ID + " INTEGER " +
                ");");

        db.execSQL("CREATE TABLE " + TABLE_TAKE_TIME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DAY_ID + " INTEGER, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_COMMENT + " TEXT default 'Тут может быть ваш комментарий', " +
                COLUMN_TASK_ID + " INTEGER " +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_GOALS);
        db.execSQL("drop table if exists " + TABLE_GROUPS);
        db.execSQL("drop table if exists " + TABLE_TASKS);
        db.execSQL("drop table if exists " + TABLE_DAYS);
        db.execSQL("drop table if exists " + TABLE_TAKE_TIME);
        db.execSQL("drop table if exists " + TABLE_DAY_TASKS);

        onCreate(db);
    }
}
