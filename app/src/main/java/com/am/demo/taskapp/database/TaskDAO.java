package com.am.demo.taskapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.am.demo.taskapp.model.MiniTask;
import com.am.demo.taskapp.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malbor806 on 28.04.2017.
 */

public class TaskDAO {
    private static final String TASKS = "tasks";
    private static final String TASK_ID = "id";
    private static final String TASK_TITLE = "task_title";
    private static final String TASK_DESCRIPTION = "task_description";
    private static final String CHECKLIST = "checklist";
    private static final String CHECK_ID = "idCheck";
    private static final String CHECK_ISCHECKED = "is_checked";
    private static final String CHECK_NAME = "check_task";
    private static TaskDAO instance;
    private static int idCounter;
    private final DBHelper dbHelper;
    private static ContentValues contentValues;
    private static Cursor cursor;

    private TaskDAO(Context context) {
        dbHelper = new DBHelper(context);
        idCounter = 0;
    }

    public static TaskDAO getInstance(Context context) {
        if(instance == null) {
            instance = new TaskDAO(context);
        }
        return instance;
    }

    public void insertNewTask(final Task task) {
        contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, task.getTitle());
        contentValues.put(TASK_DESCRIPTION, task.getDescription());
        dbHelper.getWritableDatabase().insert(TASKS, null, contentValues);
    }

    public void insertMiniTaskList(ArrayList<String> tasks, int id) {
        for (String t :tasks){
            contentValues = new ContentValues();
            contentValues.put(TASK_ID, id);
            contentValues.put(CHECK_NAME, t);
            dbHelper.getWritableDatabase().insert(CHECKLIST, null, contentValues);
        }
    }

    public int generateID(){
        return idCounter + 1;
    }

    public int generateCounter() {
        cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + TASKS + " ORDER BY " + TASK_ID + " DESC LIMIT 1", null);
        cursor.moveToFirst();
        int maxid = cursor.getInt(cursor.getColumnIndex(TASK_ID));
        return maxid;
    }

    public Task getTaskById(final int id) {
        cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + TASKS + " WHERE " + TASK_ID + " = " + id, null);
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
        return generateTask(cursor, idColumnId, titleColumnId, descriptionColumnId);
    }

    @NonNull
    private Task generateTask(Cursor cursor, int idColumnId, int titleColumnId, int descriptionColumnId) {
        Task task = new Task();
        task.setId(cursor.getInt(idColumnId));
        task.setTitle(cursor.getString(titleColumnId));
        task.setDescription(cursor.getString(descriptionColumnId));
        return task;
    }

    public void updateTask(final Task task) {
        contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, task.getTitle());
        contentValues.put(TASK_DESCRIPTION, task.getDescription());
        String id = String.valueOf(task.getId());
        dbHelper.getWritableDatabase().update(TASKS, contentValues,
                " " + TASK_ID + " = ? ",
                new String[]{id}
        );
    }

    public void deleteTaskById(final int id) {
        dbHelper.getWritableDatabase().delete(TASKS,
                " " + TASK_ID + " = ? ",
                new String[]{String.valueOf(id)}
        );
        deleteMiniTaskList(id);
    }

    public void deleteMiniTaskList(int id) {
        dbHelper.getWritableDatabase().delete(CHECKLIST,
                " " + TASK_ID + " = ? ",
                new String[]{String.valueOf(id)}
        );
    }

    public List<Task> getAllTasks() {
        cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + TASKS + ";", null);
        List<Task> results = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToTask(cursor));
            }
        }
        return results;
    }

    public List<MiniTask> getAllMiniTasks(int id) {
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM " + CHECKLIST + " WHERE " + TASK_ID + " = " + id, null);
        List<MiniTask> results = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToCheck(cursor));
            }
        }
        return results;
    }

    private MiniTask mapCursorToCheck(Cursor cursor) {
        int idColumnId = cursor.getColumnIndex(CHECK_ID);
        int isCheckedColumnId = cursor.getColumnIndex(CHECK_ISCHECKED);
        int nameColumnId = cursor.getColumnIndex(CHECK_NAME);
        return generateMiniTask(cursor, idColumnId, isCheckedColumnId, nameColumnId);
    }

    @NonNull
    private MiniTask generateMiniTask(Cursor cursor, int idColumnId, int isCheckedColumnId, int nameColumnId) {
        MiniTask miniTask = new MiniTask();
        miniTask.setId(cursor.getInt(idColumnId));
        miniTask.setChecked(cursor.getInt(isCheckedColumnId) > 0);
        miniTask.setName(cursor.getString(nameColumnId));
        return miniTask;
    }

    public void addAndUpdateMiniTaskList(ArrayList<MiniTask> miniTasks, int id){
        deleteMiniTaskList(id);
        updateMiniTaskList(miniTasks, id);
    }

    private void updateMiniTaskList(ArrayList<MiniTask> miniTasks, int taskId) {
        for (MiniTask mt : miniTasks) {
            insertUpdatedTaskList(mt, taskId);
        }
    }

    public void updateMiniTask(final MiniTask miniTask) {
        contentValues = new ContentValues();
        contentValues.put(CHECK_ISCHECKED, miniTask.isChecked());
        contentValues.put(CHECK_NAME, miniTask.getName());
        String id = String.valueOf(miniTask.getId());
        dbHelper.getWritableDatabase().update(CHECKLIST, contentValues,
                " " + CHECK_ID + " = ? ",
                new String[]{id}
        );
    }

    private void insertUpdatedTaskList(MiniTask miniTask, int id) {
        contentValues = new ContentValues();
        contentValues.put(TASK_ID, id);
        contentValues.put(CHECK_NAME, miniTask.getName());
        contentValues.put(CHECK_ISCHECKED, miniTask.isChecked());
        dbHelper.getWritableDatabase().insert(CHECKLIST, null, contentValues);
    }
}
