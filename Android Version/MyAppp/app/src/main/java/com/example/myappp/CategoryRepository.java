package com.example.myappp;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private static CategoryRepository instance;
    private static final String PREFS_NAME = "categories_prefs";
    private static final String KEY_CATEGORIES = "categories";
    private final Context context;

    private CategoryRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public static CategoryRepository get(Context context) {
        if (instance == null) {
            instance = new CategoryRepository(context);
        }
        return instance;
    }

    public void saveCategories(List<CategoryItem> categories) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(categories);
        editor.putString(KEY_CATEGORIES, json);
        editor.apply();
    }

    public void getCategories(CategoryFetchCallback callback) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_CATEGORIES, null);
        List<CategoryItem> categories;
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CategoryItem>>() {}.getType();
            categories = gson.fromJson(json, type);
        } else {
            categories = new ArrayList<>();
        }
        callback.onFetched(categories);
    }

    public interface CategoryFetchCallback {
        void onFetched(List<CategoryItem> categories);
    }
}
