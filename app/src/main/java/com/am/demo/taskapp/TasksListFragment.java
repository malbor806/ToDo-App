package com.am.demo.taskapp;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksListFragment extends Fragment {
    private static final String TASK_ID = "TASK_ID";
    @BindView(R.id.rv_taskList)
    RecyclerView tasksRecyclerView;
    @BindView(R.id.fab_addNewTask)
    FloatingActionButton addNewTaskFloatingActionButton;
    private TasksRecyclerViewAdapter adapter;
    private TaskDAO taskDAO;
    private ArrayList<Task> tasks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View fragmentView = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskDAO = TaskDAO.getInstance(getContext());
        setListeners();
        setRecyclerView();
    }

    private void setListeners() {
        addNewTaskFloatingActionButton.setOnClickListener(v -> MainActivity.start(getContext(), null));
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

    private void removeFragmentIfExist(int position) {
        View fragmentInformationView = getActivity().findViewById(R.id.ll_fragmentInformation);
        if (fragmentInformationView != null) {
            popFromBackStack();
        }
        TaskDAO taskDAO = TaskDAO.getInstance(getContext());
        taskDAO.deleteTaskById(tasks.get(position).getId());
    }

    private void showTaskDetails(Task task) {
        popFromBackStack();
        InformationFragment informationFragment = new InformationFragment();
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
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragmentInformation,
                informationFragment, MainActivity.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        tasks = (ArrayList<Task>) taskDAO.getAllTasks();
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();
    }

}
