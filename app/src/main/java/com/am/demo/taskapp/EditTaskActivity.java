package com.am.demo.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTaskActivity extends AppCompatActivity {
    private static final String TASK_ID = "TASK_ID";
    private static final String MINI_TASK_LIST = "MINI_TASK_LIST";
    private static TaskDAO taskDAO;
    @BindView(R.id.et_setTitle)
    EditText titleEditText;
    @BindView(R.id.et_setDescription)
    EditText descriptionEditText;
    @BindView(R.id.b_saveChanges)
    Button saveChangesButton;
    @BindView(R.id.ll_minitaskList)
    LinearLayout checkboxListLinearLayout;
    private LinearLayout todoLinearList;
    private List<MiniTask> miniTaskList;
    private ArrayList<String> miniTasks;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);
        taskDAO = TaskDAO.getInstance(getBaseContext());
        setSaveButtonListener();
        generateTaskIfExist(savedInstanceState);
    }

    private void setSaveButtonListener() {
        saveChangesButton.setOnClickListener(v -> {
            if (isTitleCorrect()) {
                Toast.makeText(getBaseContext(), "Title cannot be empty!", Toast.LENGTH_SHORT).show();
            } else {
                addNewTaskToDatabase();
            }
        });
    }

    private boolean isTitleCorrect(){
        String title = titleEditText.getText().toString().trim();
        return title.length() == 0;
    }

    private void generateTaskIfExist(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            generateTask(1);
            miniTasks = new ArrayList<>();
            createCheckBoxList();
        } else {
            generateTask(0);
        }
    }

    private void addNewTaskToDatabase() {
        if (task == null) {
            createNewTask();
        } else {
            updateTask();
        }
        finishActivity();
    }

    private void createNewTask() {
        task = createTask();
        taskDAO.insertNewTask(task);
        int id = taskDAO.generateCounter();
        saveMiniTaskCheckList();
        taskDAO.insertMiniTaskList(miniTasks, id);
    }

    private void updateTask() {
        task.setTitle(String.valueOf(titleEditText.getText()));
        task.setDescription(String.valueOf(descriptionEditText.getText()));
        taskDAO.updateTask(task);
        updateCheckList();
        taskDAO.addAndUpdateMiniTaskList((ArrayList<MiniTask>) miniTaskList, task.getId());
    }

    private void finishActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ID", task.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void generateTask(int returnInformationWhenOrientationChange) {
        Intent intent = getIntent();
        if (intent != null) {
            int id = intent.getIntExtra(TASK_ID, 0);
            if (id != 0) {
                task = taskDAO.getTaskById(id);
                miniTaskList = taskDAO.getAllMiniTasks(task.getId());
                if (returnInformationWhenOrientationChange == 1)
                    setEditInformation(task);
            }
        }
    }

    private void createTodoLinearList() {
        View inflatedView = getLayoutInflater().inflate(R.layout.minitask_layout, null);
        todoLinearList = (LinearLayout) inflatedView.findViewById(R.id.ll_todoItem);
        EditText editText = (EditText) todoLinearList.getChildAt(1);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createNewMiniTaskEditText(s, editText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void createNewMiniTaskEditText(CharSequence s, EditText et) {
        if (s.toString().contains("\n") || s.toString().length() == 120) {
            et.setText(et.getText().subSequence(0, et.length() - 1));
            createCheckBoxList();
            LinearLayout ll = (LinearLayout) checkboxListLinearLayout.getChildAt(checkboxListLinearLayout.getChildCount() - 1);
            ll.getChildAt(1).requestFocus();
        }
    }

    private void setEditInformation(Task task) {
        titleEditText.setText(task.getTitle());
        descriptionEditText.setText(task.getDescription());
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

    private void restoreMiniTask(String name) {
        createTodoLinearList();
        EditText et = (EditText) todoLinearList.getChildAt(1);
        et.setText(name);
        checkboxListLinearLayout.addView(todoLinearList);
    }

    private void updateCheckList() {
        int counter = 0;
        counter = updateAnExistingMiniTaskList(counter);
        addNewMiniTaskToList(counter);
    }

    private int updateAnExistingMiniTaskList(int counter) {
        for (int i = 0; i < miniTaskList.size(); i++) {
            if (miniTaskList.get(i).getName().length() > 0) {
                LinearLayout ll = (LinearLayout) checkboxListLinearLayout.getChildAt(counter);
                EditText et = (EditText) ll.getChildAt(1);
                miniTaskList.get(i).setName(String.valueOf(et.getText()));
                counter++;
            }
        }
        return counter;
    }

    private void addNewMiniTaskToList(int counter) {
        for (int i = counter; i < checkboxListLinearLayout.getChildCount(); i++) {
            MiniTask miniTask = new MiniTask();
            LinearLayout ll = (LinearLayout) checkboxListLinearLayout.getChildAt(counter);
            EditText et = (EditText) ll.getChildAt(1);
            miniTask.setName(String.valueOf(et.getText()));
            miniTaskList.add(miniTask);
            counter++;
        }
    }

    private Task createTask() {
        Task task = new Task();
        int id = taskDAO.generateID();
        task.setId(id);
        task.setTitle(String.valueOf(titleEditText.getText()));
        task.setDescription(String.valueOf(descriptionEditText.getText()));
        return task;
    }

    private void createCheckBoxList() {
        createTodoLinearList();
        checkboxListLinearLayout.addView(todoLinearList);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        saveMiniTaskCheckList();
        savedInstanceState.putStringArrayList(MINI_TASK_LIST, miniTasks);
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
        miniTasks = savedInstanceState.getStringArrayList(MINI_TASK_LIST);
        restoreMiniTaskList();
        createCheckBoxList();
    }

    private void restoreMiniTaskList() {
        for (int i = 0; i < miniTasks.size(); i++){
            if (miniTasks.get(i).length() > 0) {
                restoreMiniTask(miniTasks.get(i));
            }
        }
    }

}
