package com.am.demo.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.am.demo.taskapp.database.TaskDAO;
import com.am.demo.taskapp.model.MiniTask;
import com.am.demo.taskapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button saveChangesButton;
    private LinearLayout checkboxListLinearLayout;
    private LinearLayout todoLinearList;
    private Button addNewMiniTaskButton;
    private Intent intent;
    private Task task;
    private List<MiniTask> miniTaskList;
    private ArrayList<String> miniTasks;
    private static TaskDAO taskDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        findViews();
        taskDAO = TaskDAO.getInstance(getBaseContext());
        intent = getIntent();
        if (intent != null) {
            int id = intent.getIntExtra("TASK", 0);
            if (id != 0) {
                task = taskDAO.getTaskById(id);
                setEditInformation(task);
            }
        }
        setListener();
        if (savedInstanceState == null) {
            miniTasks = new ArrayList<>();
            View inflatedView = getLayoutInflater().inflate(R.layout.minitask_layout, null);
            todoLinearList = (LinearLayout) inflatedView.findViewById(R.id.ll_todoItem);
            checkboxListLinearLayout.addView(todoLinearList);
        }
    }

    private void findViews() {
        titleEditText = (EditText) findViewById(R.id.et_setTitle);
        descriptionEditText = (EditText) findViewById(R.id.et_setDescription);
        saveChangesButton = (Button) findViewById(R.id.b_saveChanges);
        checkboxListLinearLayout = (LinearLayout) findViewById(R.id.ll_minitaskList);
        addNewMiniTaskButton = (Button) findViewById(R.id.b_addNewMiniTask);

    }

    private void setEditInformation(Task task) {
        titleEditText.setText(task.getTitle());
        descriptionEditText.setText(task.getDescription());
        miniTaskList = taskDAO.getAllMiniTasks(task.getId());
        if (miniTaskList != null) {
            restoreMiniTaskName();
        }
    }

    private void restoreMiniTaskName() {
        for (int i = 0; i < miniTaskList.size(); i++) {
            if (miniTaskList.get(i).getName().length() > 0) {
                restoreMiniTask(miniTaskList.get(i).getName());
            }
        }
    }

    private void setListener() {
        saveChangesButton.setOnClickListener(v -> {
            if (titleEditText.getText().length() == 0) {
                Toast.makeText(getBaseContext(), "Title cannot be empty!", Toast.LENGTH_SHORT).show();
            } else {
                addNewTaskToDatabase();
            }
        });
        addNewMiniTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCheckBoxList();
            }
        });
    }


    private void addNewTaskToDatabase() {
        if (task == null) {
            task = createNewTask();
            taskDAO.insertTask(task);
        } else {
            task.setTitle(String.valueOf(titleEditText.getText()));
            task.setDescription(String.valueOf(descriptionEditText.getText()));
            taskDAO.updateTask(task);
            updateCheckList();
            taskDAO.addAndUpdateMiniTaskList((ArrayList<MiniTask>) miniTaskList, task.getId());
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ID", task.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateCheckList() {
        for (int i = 0; i < miniTaskList.size(); i++) {
            LinearLayout ll = (LinearLayout) checkboxListLinearLayout.getChildAt(i);
            EditText et = (EditText) ll.getChildAt(1);
            miniTaskList.get(i).setName(String.valueOf(et.getText()));
        }
        for (int i = miniTaskList.size(); i < checkboxListLinearLayout.getChildCount(); i++) {
            MiniTask miniTask = new MiniTask();
            LinearLayout ll = (LinearLayout) checkboxListLinearLayout.getChildAt(i);
            EditText et = (EditText) ll.getChildAt(1);
            miniTask.setName(String.valueOf(et.getText()));
            miniTaskList.add(miniTask);
        }
    }

    private Task createNewTask() {
        Task task = new Task();
        int id = taskDAO.getDatabaseSize() + 1;
        task.setId(id);
        task.setTitle(String.valueOf(titleEditText.getText()));
        task.setDescription(String.valueOf(descriptionEditText.getText()));
        saveMiniTaskCheckList();
        taskDAO.insertTaskList(miniTasks, task.getId());
        return task;
    }


    private void createCheckBoxList() {
        View inflatedView = getLayoutInflater().inflate(R.layout.minitask_layout, null);
        todoLinearList = (LinearLayout) inflatedView.findViewById(R.id.ll_todoItem);
        checkboxListLinearLayout.addView(todoLinearList);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        saveMiniTaskCheckList();
        savedInstanceState.putStringArrayList("miniTasks", miniTasks);
    }

    private void saveMiniTaskCheckList() {
        miniTasks = new ArrayList<>();
        for(int i= 0; i< checkboxListLinearLayout.getChildCount(); i++){
            LinearLayout ll = (LinearLayout) checkboxListLinearLayout.getChildAt(i);
            EditText et = (EditText) ll.getChildAt(1);
            miniTasks.add(String.valueOf(et.getText()));
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        miniTasks = savedInstanceState.getStringArrayList("miniTasks");
        restoreMiniTaskList();
    }

    private void restoreMiniTaskList() {
        for (int i = 0; i < miniTasks.size(); i++){
            if(miniTasks.get(i).length() > 0)
                restoreMiniTask(miniTasks.get(i));
        }
    }

    private void restoreMiniTask(String name){
        View inflatedView = getLayoutInflater().inflate(R.layout.minitask_layout, null);
        todoLinearList = (LinearLayout) inflatedView.findViewById(R.id.ll_todoItem);
        EditText et = (EditText) todoLinearList.getChildAt(1);
        et.setText(name);
        checkboxListLinearLayout.addView(todoLinearList);
    }


}
