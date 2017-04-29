package com.am.demo.taskapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.am.demo.taskapp.database.TaskDAO;
import com.am.demo.taskapp.model.MiniTask;
import com.am.demo.taskapp.model.Task;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {
    private static final String TASK = "TASK";
    private TextView titleTextView;
    private TextView descriptionTextView;
    private Button editTaskButton;
    private LinearLayout checkboxListLinearLayout;
    private Task task;
    private TaskDAO taskDAO;
    private List<MiniTask> miniTasks;

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

    public void setInformation() {
        if(getArguments() != null){
            int id = (int) getArguments().get("TASK");
            task = taskDAO.getTaskById(id);
            miniTasks = taskDAO.getAllMiniTasks(id);
            if(task != null){
                titleTextView.setText(task.getTitle());
                descriptionTextView.setText(task.getDescription());
                if (miniTasks != null) {
                    addCheckBoxList();
                }
            }
        }
    }

    private void addCheckBoxList() {
        checkboxListLinearLayout.removeAllViews();
        for (MiniTask mt : miniTasks) {
            if (mt.getName().length() > 0) {
                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setChecked(mt.isChecked());
                checkBox.setText(mt.getName());
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mt.setChecked(isChecked);
                    }
                });
                checkboxListLinearLayout.addView(checkBox);
            }
        }
    }

    private void setListener() {
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                intent.putExtra(TASK, task.getId());
                getActivity().startActivity(intent);
                //  getActivity().startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setInformation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (MiniTask mt : miniTasks)
            taskDAO.updateMiniTask(mt);
    }



}
