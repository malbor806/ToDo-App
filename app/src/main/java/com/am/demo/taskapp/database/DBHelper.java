package com.am.demo.taskapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by malbor806 on 28.04.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "AppDB.db";
    private static final String TASKS = "tasks";
    private static final String TASK_ID = "_id";
    private static final String TASK_TITLE = "task_title";
    private static final String TASK_DESCRIPTION = "task_description";
    private static final String TASK_LIST = "task_list";
    private static final String CHECKLIST = "checklist";
    private static final String CHECK_ID = "_idCheck";
    private static final String CHECK_ISCHECKED = "is_checked";
    private static final String CHECKTASK = "check_task";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "
                        + TASKS
                        + " ( "
                        + TASK_ID
                        + " integer primary key autoincrement, "
                        + TASK_TITLE
                        + " text, "
                        + TASK_DESCRIPTION
                        + " text, "
                        + TASK_LIST
                        + " text)"
        );
        db.execSQL(
                "create table "
                + CHECKLIST
                + " ( "
                + CHECK_ID
                + " integer primary key autoincrement, "
                + TASK_ID
                + " integer, "
                + CHECK_ISCHECKED
                + " boolean default false, "
                + CHECKTASK
                + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + CHECKLIST);
        onCreate(db);
    }
}
