package com.example.myappp;

public class TaskItem {
    public String name;
    public boolean checked;
    public long startTime; // when task was created/started
    public long endTime;   // when task was completed

    public TaskItem() {}

    public TaskItem(String name, boolean checked, long startTime, long endTime) {
        this.name = name;
        this.checked = checked;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
