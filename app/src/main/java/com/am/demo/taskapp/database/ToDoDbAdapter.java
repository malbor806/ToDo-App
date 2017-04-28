package com.am.demo.taskapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.am.demo.taskapp.model.Task;

/**
 * Created by malbor806 on 25.04.2017.
 */

public class ToDoDbAdapter {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    private static final String DB_TODO_TABLE = "todo";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;

    public static final String KEY_TITLE = "title";
    public static final String TITLE_OPTIONS = "TEXT NOT NULL";
    public static final int TITLE_COLUMN = 1;

    public static final String KEY_DESCRIPTION = "description";
    public static final String DESCRIPTION_OPTIONS = "TEXT";
    public static final int DESCRIPTION_COLUMN = 2;

    public static final String KEY_LIST = "task List";
    public static final String LIST_OPTIONS = "TEXT";
    public static final int LIST_COLUMN = 3;

    private static final String DB_CREATE_TODO_TABLE =
            "CREATE TABLE " + DB_TODO_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_TITLE + " " + TITLE_OPTIONS + ", " +
                    KEY_DESCRIPTION + " " + DESCRIPTION_OPTIONS + ", " +
                    KEY_LIST + " " + LIST_OPTIONS +
                    ");";
    private static final String DROP_TODO_TABLE =
            "DROP TABLE IF EXISTS " + DB_TODO_TABLE;


    private SQLiteDatabase database;
    private Context context;
    private DatabaseHelper dbHelper;


    public ToDoDbAdapter(Context context) {
        this.context = context;
    }

    public ToDoDbAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            database = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertTodo(Task task) {
        ContentValues newTodoValues = new ContentValues();
        newTodoValues.put(KEY_TITLE, task.getTitle());
        newTodoValues.put(KEY_DESCRIPTION, task.getDescription());
        newTodoValues.put(KEY_LIST, String.valueOf(task.getTaskList()));
        return database.insert(DB_TODO_TABLE, null, newTodoValues);
    }


    public boolean updateTodo(long id, String title, String description, String list) {
        String where = KEY_ID + "=" + id;
        ContentValues updateTodoValues = new ContentValues();
        updateTodoValues.put(KEY_TITLE, title);
        updateTodoValues.put(KEY_DESCRIPTION, description);
        updateTodoValues.put(KEY_LIST, list);
        return database.update(DB_TODO_TABLE, updateTodoValues, where, null) > 0;
    }

    public boolean deleteTodo(long id){
        String where = KEY_ID + "=" + id;
        return database.delete(DB_TODO_TABLE, where, null) > 0;
    }

    public Cursor getAllTodos() {
        String[] columns = {KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_LIST};
        return database.query(DB_TODO_TABLE, columns, null, null, null, null, null);
    }

    public Task getTodo(long id) {
        String[] columns = {KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_LIST};
        String where = KEY_ID + "=" + id;
        Cursor cursor = database.query(DB_TODO_TABLE, columns, where, null, null, null, null, null);
        Task task = null;
        if(cursor != null && cursor.moveToFirst()) {
            String description = cursor.getString(DESCRIPTION_COLUMN);
           // task = new TodoTask(id, description, completed);
        }
        return task;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TODO_TABLE);
            //Log.d(DEBUG_TAG, "Database creating...");
            //Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TODO_TABLE);
         //   Log.d(DEBUG_TAG, "Database updating...");
          //  Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
           // Log.d(DEBUG_TAG, "All data is lost.");
            onCreate(db);
        }
    }
}
