package com.example.oasisproject;

public class RecipeData {
    private String name;
    private int image;
    private String item;
    private String url;

    public RecipeData(String name, int image, String item, String url){
        this.name = name;
        this.image =image;
        this.item = item;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public String getItem() {
        return item;
    }

    public String getUrl() {
        return url;
    }
}
