package com.am.demo.taskapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.am.demo.taskapp.database.TaskDAO;
import com.am.demo.taskapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {
    private static final String TASK = "TASK";
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button saveChangesButton;
    private CheckBox addCheckListCheckBox;
    private LinearLayout checkboxListLinearLayout;
    private LinearLayout listLinearLayout;
    private Intent intent;
    private List<Task> tasks;
    private Task task;
    private static TaskDAO taskDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        findViews();
        taskDAO = TaskDAO.getInstance(getBaseContext());
        intent = getIntent();
        if (intent != null) {
            task = intent.getParcelableExtra("TASK");
            tasks = intent.getParcelableArrayListExtra("TASKS");
            if (task != null) {
                setEditInformation(task);
            }
        }
        setListener();
    }

    private void setEditInformation(Task task) {
        titleEditText.setText(task.getTitle());
        descriptionEditText.setText(task.getDescription());
    }

    private void findViews() {
        titleEditText = (EditText) findViewById(R.id.et_setTitle);
        descriptionEditText = (EditText) findViewById(R.id.et_setDescription);
        saveChangesButton = (Button) findViewById(R.id.b_saveChanges);
        addCheckListCheckBox = (CheckBox) findViewById(R.id.cb_addCheckList);
        checkboxListLinearLayout = (LinearLayout) findViewById(R.id.ll_checkboxList);
    }

    private void setListener() {
        saveChangesButton.setOnClickListener(v -> {
            if (task == null) {
                task = createNewTask();
                taskDAO.insertTask(task);
                tasks.add(task);
            } else {
                task.setTitle(String.valueOf(titleEditText.getText()));
                task.setDescription(String.valueOf(descriptionEditText.getText()));
                tasks.set(task.getId(), task);
            }
            addList(task);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putParcelableArrayListExtra("TASKS", (ArrayList<? extends Parcelable>) tasks);
            intent.putExtra("ID", task.getId());
            setResult(RESULT_OK, intent);
            finish();
        });
        addCheckListCheckBox.setOnClickListener(v -> {
            if (addCheckListCheckBox.isChecked()) {
                if (checkboxListLinearLayout.getChildCount() > 0)
                    checkboxListLinearLayout.setVisibility(View.VISIBLE);
                else {
                    createFirstCheckBox();
                }
            } else {
                checkboxListLinearLayout.setVisibility(View.GONE);
            }

        });
    }

    private void createFirstCheckBox() {
        listLinearLayout = new LinearLayout(this);
        CheckBox cb = new CheckBox(this);
        EditText et = new EditText(this);
        listLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        listLinearLayout.addView(cb);
        listLinearLayout.addView(et);
        checkboxListLinearLayout.addView(listLinearLayout);
        Button b = new Button(this);
        b.setText("ADD");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCheckBoxList();
            }
        });
    }



    private void addList(Task task) {
        ArrayList<String> e = task.getTaskList();
        for (int i = 0; i < checkboxListLinearLayout.getChildCount(); i++) {
            CheckBox c = (CheckBox) checkboxListLinearLayout.getChildAt(i);
            e.add((String) c.getText());
        }
        task.setTaskList(e);
    }

    private void createCheckBoxList() {
        listLinearLayout = new LinearLayout(this);
        CheckBox cb = new CheckBox(this);
        EditText et = new EditText(this);
        listLinearLayout.addView(cb);
        listLinearLayout.addView(et);
        checkboxListLinearLayout.addView(listLinearLayout);
    }

    private Task createNewTask() {
        Task task = new Task();
        task.setTitle(String.valueOf(titleEditText.getText()));
        task.setDescription(String.valueOf(descriptionEditText.getText()));
        return task;
    }


}
