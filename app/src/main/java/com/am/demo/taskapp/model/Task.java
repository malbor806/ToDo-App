package com.am.demo.taskapp.model;

/**
 * Created by malbor806 on 23.04.2017.
 */

public class Task {
    private int id;
    private String title;
    private String description;

    public Task() {
        id = 0;
        title = "";
        description = "";
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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

}
