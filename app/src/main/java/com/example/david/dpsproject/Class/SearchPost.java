package com.example.david.dpsproject.Class;

/**
 * Created by david on 2016-12-26.
 */

public class SearchPost {

    private String key;
    private String Category;
    private String title;

    public SearchPost(){

    }

    public SearchPost(String s, String c, String t){
        key=s;
        Category=c;
        title=t;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
