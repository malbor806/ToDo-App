package com.am.demo.taskapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            startNewTaskListFragment();
        } else {
            checkIfRootFragmentIsNotNull(savedInstanceState);
        }
    }

    private void checkIfRootFragmentIsNotNull(Bundle savedInstaceState) {
        if (findViewById(R.id.container_rootFragment) != null) {
            if (savedInstaceState != null)
                return;
            startNewTaskListFragment();
        }
    }

    private void startNewTaskListFragment() {
        if (findViewById(R.id.container_fragmentTasksList) != null) {
            TasksListFragment tasksListFragment = new TasksListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragmentTasksList, tasksListFragment, TAG).commit();
        }
    }

}
