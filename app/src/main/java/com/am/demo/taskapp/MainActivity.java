package com.am.demo.taskapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.am.demo.taskapp.model.Task;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private static final String TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            startNewTaskListFragment();
        } else {
            startNewTaskListFragmentIfRootFragmentIsNotNull(savedInstanceState);
        }
    }

    private void startNewTaskListFragmentIfRootFragmentIsNotNull(Bundle savedInstanceState) {
        if (findViewById(R.id.container_rootFragment) != null) {
            if (savedInstanceState != null)
                return;
            if (findViewById(R.id.container_fragmentTasksList) != null) {
                startNewTaskListFragment();
            }
        }
    }

    private void startNewTaskListFragment() {
            TasksListFragment tasksListFragment = new TasksListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragmentTasksList, tasksListFragment, TAG).commit();
    }

    public static void start(Context context, Task task) {
        Intent starter = new Intent(context, EditTaskActivity.class);
        if (task != null) {
            starter.putExtra(TASK_ID, task.getId());
        }
        context.startActivity(starter);
    }
}
