package com.example.oasisproject;

// 재료 기본 정보
public class ItemData{
    private String name;
    private int money;
    private int number;
    private int until_year;
    private int until_month;
    private int until_day;
    private String type;

    public ItemData(String name, int money, int number, int until_year, int until_month, int until_day, String type){
        this.name =name;
        this.money=money;
        this.number=number;
        this.until_year = until_year;
        this.until_month=until_month;
        this.until_day=until_day;
        this.type = type;
    }

    public int getYear() {
        return until_year;
    }

    public int getDay() {
        return until_day;
    }

    public int getMoney() {
        return money;
    }

    public int getMonth() {
        return until_month;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
