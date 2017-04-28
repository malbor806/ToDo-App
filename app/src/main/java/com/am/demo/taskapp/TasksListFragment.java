package com.am.demo.taskapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.am.demo.taskapp.adapter.TasksRecyclerViewAdapter;
import com.am.demo.taskapp.database.TaskDAO;
import com.am.demo.taskapp.model.Task;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {
    private static final String TASK = "TASK";
    private static final int REQUEST_CODE = 100;
    private RecyclerView tasksRecyclerView;
    private FloatingActionButton addNewTaskFloatingActionButton;
    private List<Task> tasks;
    private TasksRecyclerViewAdapter adapter;
    private InformationFragment informationFragment;
    private SharedPreferences sharedPrefences;
    private Task task;
    private TaskDAO taskDAO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);

        return inflater.inflate(R.layout.fragment_tasks_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskDAO = TaskDAO.getInstance(getContext());

        if (getArguments() != null) {
            tasks = getArguments().getParcelableArrayList("TASKS");
        }
        else{
            tasks = new ArrayList<>();
        }

        findViews();
        setRecyclerView();
        setListeners();
    }


    private void findViews() {
        tasksRecyclerView = (RecyclerView) getView().findViewById(R.id.rv_taskList);
        addNewTaskFloatingActionButton = (FloatingActionButton) getView().findViewById(R.id.fab_addNewTask);
    }

    private void setRecyclerView() {
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TasksRecyclerViewAdapter();
        tasksRecyclerView.setAdapter(adapter);
        adapter.setOnTaskClickListener(this::showTaskDetails);
        adapter.setTasks(taskDAO.getAllNotes());
    }


    private void showTaskDetails(Task task) {
        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.container_fragmentInformation) != null) {

            getActivity().getSupportFragmentManager().popBackStack();

        }
        informationFragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putParcelable(TASK, task);
        args.putParcelableArrayList("TASKS", (ArrayList<? extends Parcelable>) tasks);
        informationFragment.setArguments(args);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragmentInformation ,
                informationFragment, MainActivity.TAG);

        int currentOrientation = getResources().getConfiguration().orientation;
      //  if (currentOrientation != Configuration.ORIENTATION_LANDSCAPE) {
            transaction.addToBackStack(TasksListFragment.class.getName());
       // }
        transaction.commit();
    }

    private boolean isLandscape() {
        return getView().findViewById(R.id.b_editButton) != null;
    }

    private void setListeners() {
        addNewTaskFloatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditTaskActivity.class);
            intent.putParcelableArrayListExtra("TASKS", (ArrayList<? extends Parcelable>) tasks);
            getActivity().startActivityForResult(intent, 100);
        });
    }

}
