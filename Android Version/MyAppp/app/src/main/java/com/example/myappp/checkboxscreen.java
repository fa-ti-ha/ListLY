package com.example.myappp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class checkboxscreen extends AppCompatActivity {

    List<TaskItem> tasks;
    checkboxadapter adapter;
    String PREF_KEY;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = getIntent().getIntExtra("LAYOUT", R.layout.deadline);
        setContentView(layout);

        PREF_KEY = getIntent().getStringExtra("PREF_KEY");
        setTitle(getIntent().getStringExtra("TITLE"));

        db = FirebaseFirestore.getInstance();

        RecyclerView rv = findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        tasks = new ArrayList<>();
        adapter = new checkboxadapter(tasks, this::saveTasks, this);
        rv.setAdapter(adapter);

        loadTasks();

        ImageView add = findViewById(R.id.imagebutton6);
        add.setOnClickListener(v -> showAddTaskDialog());
    }

    private void loadTasks() {
        db.collection("tasks").document(PREF_KEY).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) doc.get("tasks");
                if (list != null) {
                    tasks.clear();
                    for (Map<String, Object> t : list) {
                        tasks.add(new TaskItem(
                                (String) t.get("name"),
                                (boolean) t.get("checked"),
                                (long) t.getOrDefault("startTime", 0L),
                                (long) t.getOrDefault("endTime", 0L)
                        ));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showAddTaskDialog() {
        EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Add Task")
                .setView(input)
                .setPositiveButton("Add", (d, w) -> {
                    String taskName = input.getText().toString().trim();
                    if (!taskName.isEmpty()) {
                        TaskItem t = new TaskItem(taskName, false, System.currentTimeMillis(), 0L);
                        tasks.add(t);
                        adapter.notifyItemInserted(tasks.size() - 1);
                        saveTasks();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveTasks() {
        List<Map<String, Object>> saveList = new ArrayList<>();
        for (TaskItem t : tasks) {
            Map<String, Object> m = new HashMap<>();
            m.put("name", t.name);
            m.put("checked", t.checked);
            m.put("startTime", t.startTime);
            m.put("endTime", t.endTime);
            saveList.add(m);
        }

        db.collection("tasks").document(PREF_KEY)
                .set(Map.of("tasks", saveList));
    }
}
