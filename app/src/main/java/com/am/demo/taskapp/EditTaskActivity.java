package com.am.demo.taskapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

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
    private static SharedPreferences sharedPreferences;
    private LinearLayout listLinearLayout;
    private EditText listEditText;
    private CheckBox checkBox;
    private Intent intent;
    private List<Task> tasks;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        findViews();
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
            //  EditTaskActivity.this.startActivityForResult(intent, 100);//REQUEST_CODE);
            finish();
        });
        addCheckListCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addCheckListCheckBox.isChecked()) {
                    if (checkboxListLinearLayout.getChildCount() > 0)
                        checkboxListLinearLayout.setVisibility(View.VISIBLE);
                    else {
                        createCheckBoxList();
                        createCheckBoxList();
                    }
                } else {
                    checkboxListLinearLayout.setVisibility(View.GONE);
                }

            }
        });
    }

    private void setEditTextListener(EditText editText, CheckBox checkBox) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                }
            }
        });
       /* editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = editText.getText().length();
                v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        int b = editText.getText().length();
                        if (!hasFocus) {
                            if (a!=b) {
                                createCheckBoxList();
                            }
                        }
                    }
                });
            }
        });
*/
/*
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkBox.setText(editText.getText());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/
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
        setEditTextListener(et, cb);
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
