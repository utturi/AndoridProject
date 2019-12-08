package com.example.andoridproject.Item;

public class Recipe {
    private String introduction;
    private String url;

    public Recipe(String introduction, String url) {
        this.introduction = introduction;
        this.url = url;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getUrl() {
        return url;
    }
}
