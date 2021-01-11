package com.example.oasisproject;

import java.util.Comparator;

public class SortType implements Comparator<FullItemData> {

    @Override
    public int compare(FullItemData o1, FullItemData o2) {
        return o1.getType().compareTo(o2.getType());
    }
}