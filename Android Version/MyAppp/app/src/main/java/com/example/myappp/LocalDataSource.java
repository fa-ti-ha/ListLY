package com.example.myappp;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalDataSource {

    private static final String KEY = "categories_json";
    private SharedPreferences prefs;
    private Gson gson = new Gson();

    public LocalDataSource(Context c) {
        prefs = c.getSharedPreferences("app", Context.MODE_PRIVATE);
    }

    public List<CategoryItem> load() {
        String json = prefs.getString(KEY, "");
        if (json.isEmpty()) return new ArrayList<>();

        Type t = new TypeToken<List<CategoryItem>>(){}.getType();
        return gson.fromJson(json, t);
    }

    public void save(List<CategoryItem> list) {
        prefs.edit()
                .putString(KEY, gson.toJson(list))
                .apply();
    }
}
