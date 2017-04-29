package com.am.demo.taskapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.am.demo.taskapp.database.TaskDAO;
import com.am.demo.taskapp.model.MiniTask;
import com.am.demo.taskapp.model.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {
    private static final String TASK_ID = "TASK_ID";
    private TextView titleTextView;
    private TextView descriptionTextView;
    private Button editTaskButton;
    private LinearLayout checkboxListLinearLayout;
    private Task task;
    private TaskDAO taskDAO;
    private List<MiniTask> miniTaskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View v = inflater.inflate(R.layout.fragment_information, container, false);
        v.setClickable(true);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        taskDAO = TaskDAO.getInstance(getContext());
        setListener();
    }

    private void findViews() {
        titleTextView = (TextView) getView().findViewById(R.id.tv_taskTitle);
        descriptionTextView = (TextView) getView().findViewById(R.id.tv_taskDescription);
        editTaskButton = (Button) getView().findViewById(R.id.b_editButton);
        checkboxListLinearLayout = (LinearLayout) getView().findViewById(R.id.ll_checkboxList);
    }

    private void setListener() {
        editTaskButton.setOnClickListener(v -> startEditTaskActivity());
    }

    private void startEditTaskActivity() {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);
        intent.putExtra(TASK_ID, task.getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        setInformation();
    }

    public void setInformation() {
        if(getArguments() != null){
            int id = (int) getArguments().get(TASK_ID);
            task = taskDAO.getTaskById(id);
            miniTaskList = taskDAO.getAllMiniTasks(id);
            loadInformationFromDatabase();
        }
    }

    private void loadInformationFromDatabase() {
        if (task != null) {
            titleTextView.setText(task.getTitle());
            descriptionTextView.setText(task.getDescription());
            if (miniTaskList != null) {
                addCheckBoxList();
            }
        }
    }

    private void addCheckBoxList() {
        checkboxListLinearLayout.removeAllViews();
        for (MiniTask mt : miniTaskList) {
            if (mt.getName().length() > 0) {
                CheckBox checkBox = createNewCheckBox(mt);
                checkboxListLinearLayout.addView(checkBox);
            }
        }
    }

    @NonNull
    private CheckBox createNewCheckBox(MiniTask mt) {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setChecked(mt.isChecked());
        checkBox.setText(mt.getName());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mt.setChecked(isChecked);
        });
        return checkBox;
    }

    @Override
    public void onPause() {
        super.onPause();
        taskDAO.addAndUpdateMiniTaskList((ArrayList<MiniTask>) miniTaskList, task.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (MiniTask mt : miniTaskList)
            taskDAO.updateMiniTask(mt);
    }
}
