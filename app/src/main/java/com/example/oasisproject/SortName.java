package com.example.oasisproject;

import java.util.Comparator;

public class SortName implements Comparator<FullItemData> {

    @Override
    public int compare(FullItemData o1, FullItemData o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
