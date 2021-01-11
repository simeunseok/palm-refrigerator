package com.example.oasisproject;

import java.util.Comparator;

// 전체보기 했을 때 아이템데이터
public class FullItemData {
    private String name;
    private String until;
    private int number;
    private String type;


    public FullItemData(String name, int number, String until, String type) {
        this.name = name;
        this.number = number;
        this.until = until;
        this.type = type;
    }


    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getUntil() {
        return until;
    }

    public String getType() {
        return type;
    }
}

