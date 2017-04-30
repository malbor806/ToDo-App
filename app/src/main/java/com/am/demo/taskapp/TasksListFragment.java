package com.am.demo.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.am.demo.taskapp.adapter.SimpleItemTouchHelperCallback;
import com.am.demo.taskapp.adapter.TasksRecyclerViewAdapter;
import com.am.demo.taskapp.database.TaskDAO;
import com.am.demo.taskapp.model.Task;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {
    private static final String TASK_ID = "TASK_ID";
    private RecyclerView tasksRecyclerView;
    private FloatingActionButton addNewTaskFloatingActionButton;
    private TaskDAO taskDAO;
    private InformationFragment informationFragment;
    private FragmentTransaction fragmentTransaction;
    TasksRecyclerViewAdapter adapter;

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
        setListeners();
        setRecyclerView();
    }

    private void findViews() {
        tasksRecyclerView = (RecyclerView) getView().findViewById(R.id.rv_taskList);
        addNewTaskFloatingActionButton = (FloatingActionButton) getView().findViewById(R.id.fab_addNewTask);
    }

    private void setListeners() {
        addNewTaskFloatingActionButton.setOnClickListener(v -> startEditTaskActivity());
    }

    private void startEditTaskActivity() {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);
        getActivity().startActivity(intent);
    }

    private void setRecyclerView() {
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TasksRecyclerViewAdapter(getContext());
        adapter.setOnTaskClickListener(this::showTaskDetails);
        adapter.setOnTaskRemoveListener(this::removeFragmentIfExist);
        tasksRecyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(tasksRecyclerView);
    }

    private void removeFragmentIfExist() {
        View v = getActivity().findViewById(R.id.ll_fragmentInformation);
        if (v != null) {
            popFromBackStack();
        }
    }

    private void showTaskDetails(Task task) {
        popFromBackStack();
        informationFragment = new InformationFragment();
        addArgumentsForInformationFragment(task, informationFragment);
        startInformationFragmentTransaction(informationFragment);
    }

    private void popFromBackStack() {
        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.container_fragmentInformation) != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void addArgumentsForInformationFragment(Task task, InformationFragment informationFragment) {
        Bundle args = new Bundle();
        args.putInt(TASK_ID, task.getId());
        informationFragment.setArguments(args);
    }

    private void startInformationFragmentTransaction(InformationFragment informationFragment) {
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragmentInformation,
                informationFragment, MainActivity.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setTasks(taskDAO.getAllTasks());
        adapter.notifyDataSetChanged();
    }

}
