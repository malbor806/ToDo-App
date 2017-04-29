package com.am.demo.taskapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.am.demo.taskapp.model.Task;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private static final String TASK = "TASK";
    private static final int REQUEST_CODE = 100;
    private TasksListFragment tasksListFragment;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.container_rootFragment) != null) {
            if (savedInstanceState != null) {
                return;
            }
            startNewTaskListFragment();
        }
    }

    private void startNewTaskListFragment() {
        if (findViewById(R.id.container_fragmentTasksList) != null) {
            tasksListFragment = new TasksListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragmentTasksList, tasksListFragment, TAG).commit();
        }
    }

}
