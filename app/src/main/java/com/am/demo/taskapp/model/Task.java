package com.am.demo.taskapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by malbor806 on 23.04.2017.
 */

public class Task implements Parcelable {
    public static int counter = 0;
    private int id;
    private String title;
    private String description;
    private ArrayList<String> taskList;

    public Task() {
        id = 0;
        title = "";
        description = "";
        taskList = new ArrayList<>();
    }

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        taskList = in.createStringArrayList();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getTaskList() {
        return taskList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskList(ArrayList<String> taskList) {
        this.taskList = taskList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeStringList(taskList);
    }
}
