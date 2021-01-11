package com.example.oasisproject;

import java.util.Comparator;

public class SortUntil implements Comparator<FullItemData> {

    @Override
    public int compare(FullItemData o1, FullItemData o2) {
        return o1.getUntil().compareTo(o2.getUntil());
    }
}
