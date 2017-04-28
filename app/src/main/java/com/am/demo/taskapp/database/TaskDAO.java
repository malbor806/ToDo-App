package com.am.demo.taskapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.am.demo.taskapp.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by malbor806 on 28.04.2017.
 */

public class TaskDAO {
    private static TaskDAO instance;
    private DBHelper dbHelper;
    private static final String TASKS = "tasks";
    private static final String TASK_ID = "tasks._id";
    private static final String TASK_TITLE = "task_title";
    private static final String TASK_DESCRIPTION = "task_description";
    private static final String TASK_LIST = "task_list";
    private static final String CHECKLIST = "checklist";
    private static final String CHECKTASK = "check_task";


    private TaskDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public static TaskDAO getInstance(Context context) {
        if(instance == null) {
            instance = new TaskDAO(context);
        }
        return instance;
    }

    // wstawienie nowej notatki do bazy danych
    public void insertTask(final Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, task.getTitle());
        contentValues.put(TASK_DESCRIPTION, task.getDescription());
        contentValues.put(TASK_LIST, String.valueOf(task.getTaskList()));
        dbHelper.getWritableDatabase().insert(TASKS, null, contentValues);
    }

    // pobranie notatki na podstawie jej id
    public Task getTaskById(final int id) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + TASKS + " where " + TASK_ID + " = " + id, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            return mapCursorToTask(cursor);
        }
        return null;
    }

    private Task mapCursorToTask(final Cursor cursor) {
        int idColumnId = cursor.getColumnIndex(TASK_ID);
        int titleColumnId = cursor.getColumnIndex(TASK_TITLE);
        int descriptionColumnId = cursor.getColumnIndex(TASK_DESCRIPTION);
        int listColumnId = cursor.getColumnIndex(TASK_LIST);
        Task task = new Task();
        task.setId(cursor.getInt(idColumnId));
        task.setTitle(cursor.getString(titleColumnId));
        task.setDescription(cursor.getString(descriptionColumnId));
        String tasksList = cursor.getString(listColumnId);
        List<String> myList = new ArrayList<String>(Arrays.asList(tasksList.split(",")));
        task.setTaskList((ArrayList<String>) myList);
        return task;
    }

    // aktualizacja notatki w bazie
    public void updateTask(final Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, task.getTitle());
        contentValues.put(TASK_DESCRIPTION, task.getDescription());
        contentValues.put(TASK_LIST, String.valueOf(task.getTaskList()));

        String id = String.valueOf(task.getId());
        dbHelper.getWritableDatabase().update(TASKS,
                contentValues,
                " " + TASK_ID + " = ? ",
                new String[]{id}
        );
    }

    // usunięcie notatki z bazy
    public void deleteTaskById(final int id) {
        dbHelper.getWritableDatabase().delete(TASKS,
                " " + TASK_ID + " = ? ",
                new String[]{String.valueOf(id)}
        );
    }

    // pobranie wszystkich notatek
    public List getAllTasks() {
        Cursor cursor = dbHelper.getReadableDatabase().query(TASKS,
                new String[]{TASK_ID, TASK_TITLE, TASK_DESCRIPTION,
                TASK_LIST},
                null, null, null, null, null
        );

        List results = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToTask(cursor));
            }
        }

        return results;
    }
}
