package com.example.myappp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context context;
    List<ListItem> lists;

    public ListAdapter(Context context, List<ListItem> lists) {
        this.context = context;
        this.lists = lists;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvListTitle;
        FrameLayout previewContainer;
        long lastClickTime = 0;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvListTitle = itemView.findViewById(R.id.tvListTitle);
            previewContainer = itemView.findViewById(R.id.previewContainer);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem item = lists.get(position);

        holder.tvListTitle.setText(item.title);
        holder.previewContainer.removeAllViews();

        View preview = LayoutInflater.from(context)
                .inflate(item.previewLayout, holder.previewContainer, false);
        holder.previewContainer.addView(preview);

        // Double-click to edit list title
        holder.tvListTitle.setOnClickListener(v -> {
            long clickTime = System.currentTimeMillis();
            if (clickTime - holder.lastClickTime < 300) {
                showEditListDialog(item, position);
            }
            holder.lastClickTime = clickTime;
        });

        // Single click to open checkboxscreen
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, checkboxscreen.class);
            intent.putExtra("LAYOUT", item.previewLayout);
            intent.putExtra("PREF_KEY", item.prefKey);
            intent.putExtra("TITLE", item.title);
            context.startActivity(intent);
        });
    }

    private void showEditListDialog(ListItem item, int position) {
        EditText input = new EditText(context);
        input.setText(item.title);

        new AlertDialog.Builder(context)
                .setTitle("Edit List Name")
                .setView(input)
                .setPositiveButton("Update", (d, w) -> {
                    item.title = input.getText().toString().trim();
                    notifyItemChanged(position);

                    // Save categories after editing
                    CategoryRepository.get(context).getCategories(categories -> {
                        for (CategoryItem cat : categories) {
                            for (ListItem l : cat.lists) {
                                if (l.prefKey.equals(item.prefKey)) {
                                    l.title = item.title;
                                    break;
                                }
                            }
                        }
                        CategoryRepository.get(context).saveCategories(categories);
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
