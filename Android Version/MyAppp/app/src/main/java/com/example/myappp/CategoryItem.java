package com.example.myappp;

import java.util.List;

public class CategoryItem {
    public String id;
    public String title;
    public long createdTime;
    public List<ListItem> lists;

    public CategoryItem() {
        this.createdTime = System.currentTimeMillis();
    }

    public CategoryItem(String id, String title, List<ListItem> lists) {
        this.id = id;
        this.title = title;
        this.lists = lists;
        this.createdTime = System.currentTimeMillis();
    }
}
