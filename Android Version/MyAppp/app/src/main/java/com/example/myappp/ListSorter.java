package com.example.myappp;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListSorter {

    public static void sortListsAlphabetical(List<ListItem> lists) {
        Collections.sort(lists, Comparator.comparing(l -> l.title.toLowerCase()));
    }

    public static void sortListsNewest(List<ListItem> lists) {
        // Assuming newest added are at the end
        Collections.reverse(lists);
    }

    public static void sortListsOldest(List<ListItem> lists) {
        // Assuming oldest added are at the beginning
        // No action needed
    }

    public static void sortListsByStyle(List<ListItem> lists) {
        // If you have different layouts for styles, you can sort by layout
        Collections.sort(lists, Comparator.comparingInt(l -> l.previewLayout));
    }
}
