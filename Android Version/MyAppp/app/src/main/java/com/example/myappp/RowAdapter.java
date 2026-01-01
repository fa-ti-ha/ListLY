package com.example.myappp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> {

    Context context;
    List<RowItem> rows;
    SortMode sortMode = SortMode.ALPHABETICAL;

    public RowAdapter(Context context, List<RowItem> rows) {
        this.context = context;
        this.rows = rows;
    }

    public void setSortMode(SortMode mode) {
        this.sortMode = mode;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvHorizontalLists;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvHorizontalLists = itemView.findViewById(R.id.rvHorizontalLists);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RowItem row = rows.get(position);
        List<ListItem> lists = new ArrayList<>(row.lists);

        // Sorting (you can implement ListSorter)
        if (sortMode == SortMode.ALPHABETICAL) ListSorter.sortListsAlphabetical(lists);

        holder.rvHorizontalLists.setLayoutManager(
                new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.rvHorizontalLists.setNestedScrollingEnabled(false);
        holder.rvHorizontalLists.setAdapter(new ListAdapter(context, lists));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }
}
