package com.example.myappp;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final Context context;
    private final List<CategoryItem> categories;

    public CategoryAdapter(Context context, List<CategoryItem> categories) {
        this.context = context;
        this.categories = categories;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        RecyclerView rvRows;
        long lastClickTime = 0;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            rvRows = itemView.findViewById(R.id.rvRows);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homebox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryItem category = categories.get(position);
        holder.tvCategory.setText(category.title);

        holder.tvCategory.setOnClickListener(v -> {
            long clickTime = System.currentTimeMillis();
            if (clickTime - holder.lastClickTime < 300) {
                showEditCategoryDialog(category, position);
            }
            holder.lastClickTime = clickTime;
        });

        holder.rvRows.setLayoutManager(
                new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.rvRows.setNestedScrollingEnabled(false);

        List<RowItem> rowItems = new ArrayList<>();
        rowItems.add(new RowItem(category.lists));

        holder.rvRows.setAdapter(new RowAdapter(context, rowItems));
    }

    private void showEditCategoryDialog(CategoryItem category, int position) {
        EditText input = new EditText(context);
        input.setText(category.title);

        new AlertDialog.Builder(context)
                .setTitle("Edit Category Name")
                .setView(input)
                .setPositiveButton("Update", (dialog, which) -> {
                    category.title = input.getText().toString().trim();
                    notifyItemChanged(position);
                    CategoryRepository.get(context).saveCategories(categories);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
