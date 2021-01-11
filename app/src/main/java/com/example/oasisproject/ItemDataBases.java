package com.example.oasisproject;

// 재료 아이템베이스 정보
public final class ItemDataBases {
    private ItemDataBases(){};

    public static final String TBL = "buyItems";
    public static final String NAME = "name";
    public static final String MONEY = "money";
    public static final String NUM = "number";
    public static final String UNTIL_YEAR = "until_year";
    public static final String UNTIL_MONTH = "until_month";
    public static final String UNTIL_DAY = "until_day";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String TYPE = "type";

    public static final String _CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL + " " +
            "(" +
            NAME + " TEXT NOT NULL, " +
            MONEY + " INTEGER NOT NULL, " +
            NUM + " INTEGER NOT NULL, " +
            UNTIL_YEAR + " INTEGER, " +
            UNTIL_MONTH + " INTEGER, " +
            UNTIL_DAY + " INTEGER, " +
            YEAR + " INTEGER, " +
            MONTH + " INTEGER, " +
            DAY + " INTEGER, " +
            TYPE + " INTEGER" +
            ")";

    public static final String DROP_TBL = "DROP TABLE IF EXISTS " + TBL;

    public static final String SELECT = "SELECT * FROM " + TBL;

    public static final String INSERT = "INSERT OR REPLACE INTO " + TBL + " " +
            "(" + NAME + ", " + MONEY + ", " + NUM + ", " + UNTIL_YEAR + ", " + UNTIL_MONTH + ", " + UNTIL_DAY + ", " + YEAR + ", " + MONTH + ", " + DAY + ", " + TYPE + ") VALUES" ;

    public static final String DELETE = "DELETE FROM  " + TBL;
}

