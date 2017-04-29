package com.am.demo.taskapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {
    private RecyclerView tasksRecyclerView;
    private FloatingActionButton addNewTaskFloatingActionButton;
    private TasksRecyclerViewAdapter adapter;
    private InformationFragment informationFragment;
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
        adapter.setTasks(taskDAO.getAllTasks());
    }


    private void showTaskDetails(Task task) {
        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.container_fragmentInformation) != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        informationFragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putInt("TASK", task.getId());
        informationFragment.setArguments(args);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragmentInformation ,
                informationFragment, MainActivity.TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setListeners() {
        addNewTaskFloatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditTaskActivity.class);
            getActivity().startActivity(intent);
//            getActivity().startActivityForResult(intent, 100);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerView();
    }
}
