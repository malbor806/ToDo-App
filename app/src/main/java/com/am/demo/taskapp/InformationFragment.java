package com.am.demo.taskapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {
    private static final String TASK_ID = "TASK_ID";
    private static Task task;
    @BindView(R.id.tv_taskTitle)
    TextView titleTextView;
    @BindView(R.id.tv_taskDescription)
    TextView descriptionTextView;
    @BindView(R.id.b_editButton)
    Button editTaskButton;
    @BindView(R.id.ll_checkboxList)
    LinearLayout checkboxListLinearLayout;
    private TaskDAO taskDAO;
    private List<MiniTask> miniTaskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View fragmentView = inflater.inflate(R.layout.fragment_information, container, false);
        fragmentView.setClickable(true);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskDAO = TaskDAO.getInstance(getContext());
        setListener();
    }

    private void setListener() {
        editTaskButton.setOnClickListener(v -> MainActivity.start(getContext(), task));
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
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> mt.setChecked(isChecked));
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
        for (MiniTask mt : miniTaskList) {
            taskDAO.updateMiniTask(mt);
        }
    }
}
