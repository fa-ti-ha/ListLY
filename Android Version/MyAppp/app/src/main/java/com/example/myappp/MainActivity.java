package com.example.myappp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    RecyclerView homeRecycler;
    CategoryAdapter adapter;
    List<CategoryItem> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView stopwatchBtn = findViewById(R.id.stopwatchBtn);
        stopwatchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StopwatchActivity.class);
            startActivity(intent);
        });

        homeRecycler = findViewById(R.id.homeRecycler);
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));

        categories = new ArrayList<>();
        adapter = new CategoryAdapter(this, categories);
        homeRecycler.setAdapter(adapter);

        loadCategories();

        ImageView addButton = findViewById(R.id.imagebutton66);
        addButton.setOnClickListener(v -> showAddListStyleDialog());
    }

    private void loadCategories() {
        CategoryRepository.get(this).getCategories(fetched -> {
            categories.clear();
            categories.addAll(fetched);
            adapter.notifyDataSetChanged();
        });
    }

    private void showAddListStyleDialog() {
        String[] styles = {"Checkbox", "Wishlist", "Plain List", "Note", "Memo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, styles);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select List Style")
                .setAdapter(adapter, (dialog, which) -> showCategoryChoiceDialog(styles[which]))
                .show();
    }

    private void showCategoryChoiceDialog(String listStyle) {
        List<String> categoryNames = new ArrayList<>();
        for (CategoryItem c : categories) categoryNames.add(c.title);
        categoryNames.add("+ New Category");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, categoryNames);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Choose Category")
                .setAdapter(adapter, (dialog, which) -> {
                    if (which == categoryNames.size() - 1) showCreateCategoryDialog(listStyle);
                    else showCreateListDialog(categories.get(which), listStyle);
                })
                .show();
    }

    private void showCreateCategoryDialog(String listStyle) {
        EditText input = new EditText(this);
        input.setHint("Category name");

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("New Category")
                .setView(input)
                .setPositiveButton("Next", (d, w) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        CategoryItem newCat = new CategoryItem(UUID.randomUUID().toString(), name, new ArrayList<>());
                        categories.add(newCat);
                        CategoryRepository.get(this).saveCategories(categories);
                        adapter.notifyDataSetChanged();
                        showCreateListDialog(newCat, listStyle);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showCreateListDialog(CategoryItem category, String style) {
        EditText input = new EditText(this);
        input.setHint("List name");

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("New " + style + " List in " + category.title)
                .setView(input)
                .setPositiveButton("Create", (d, w) -> {
                    String listName = input.getText().toString().trim();
                    if (!listName.isEmpty()) {
                        int layout = getLayoutByStyle(style);
                        ListItem newList = new ListItem(UUID.randomUUID().toString(), listName, layout, "list_" + UUID.randomUUID());
                        category.lists.add(newList);
                        CategoryRepository.get(this).saveCategories(categories);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private int getLayoutByStyle(String style) {
        switch (style) {
            case "Checkbox": return R.layout.homebox;
            case "Wishlist": return R.layout.shopping;
            case "Plain List": return R.layout.shopping;
            case "Note": return R.layout.travel;
            case "Memo": return R.layout.deadline;
            default: return R.layout.shopping;
        }
    }

}
