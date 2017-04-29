package com.am.demo.taskapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.am.demo.taskapp.database.TaskDAO;
import com.am.demo.taskapp.model.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {
    private static final String TASK = "TASK";
    private TextView titleTextView;
    private TextView descriptionTextView;
    private Button editTaskButton;
    private Task task;
    private TaskDAO taskDAO;


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
        setInformation();
        setListener();
    }

    private void findViews() {
        titleTextView = (TextView) getView().findViewById(R.id.tv_taskTitle);
        descriptionTextView = (TextView) getView().findViewById(R.id.tv_taskDescription);
        editTaskButton = (Button) getView().findViewById(R.id.b_editButton);
    }

    private void setInformation() {
        if(getArguments() != null){
            task = taskDAO.getTaskById((Integer) getArguments().get("TASK"));
            if(task != null){
                titleTextView.setText(task.getTitle());
                descriptionTextView.setText(task.getDescription());
            }
        }
    }

    private void setListener() {
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                intent.putExtra(TASK, task);
                getActivity().startActivityForResult(intent, 100);
            }
        });
    }

}
