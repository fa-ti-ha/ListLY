package com.example.myappp;

public class ListItem {
    public String id;
    public String title;
    public int previewLayout;
    public String prefKey;

    public ListItem() {}

    public ListItem(String id, String title, int previewLayout, String prefKey) {
        this.id = id;
        this.title = title;
        this.previewLayout = previewLayout;
        this.prefKey = prefKey;
    }
}
