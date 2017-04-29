package com.am.demo.taskapp.model;

/**
 * Created by malbor806 on 29.04.2017.
 */

public class MiniTask {
    private int id;
    private boolean isChecked;
    private String name;;

    public MiniTask(){
        id = 0;
        isChecked = false;
        name = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
