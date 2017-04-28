package com.am.demo.taskapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.am.demo.taskapp.database.TaskDAO;
import com.am.demo.taskapp.database.ToDoDbAdapter;
import com.am.demo.taskapp.model.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private static final String TASK = "TASK";
    private static final int REQUEST_CODE = 100;
    private TasksListFragment tasksListFragment;
    private static SharedPreferences sharedPreferences;
    private Task task;
    private TaskDAO taskDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.container_rootFragment) != null) {
            if (savedInstanceState != null) {
                return;
            }

            if (findViewById(R.id.container_fragmentTasksList) != null) {
                tasksListFragment = new TasksListFragment();
                if (task != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(TASK, task);
                    tasksListFragment.setArguments(bundle);
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_fragmentTasksList, tasksListFragment, TAG).commit();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    ArrayList<Task> tasks = extras.getParcelableArrayList("TASKS");
                    int id = extras.getInt("ID");
                    tasksListFragment = new TasksListFragment();
                    if (tasks != null) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("TASKS", tasks);
                        bundle.putInt("ID", id);
                        tasksListFragment.setArguments(bundle);
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_fragmentTasksList, tasksListFragment, TAG).commitAllowingStateLoss();
                }

            }
        }
    }




}
