package com.example.david.dpsproject.Class;

import com.example.david.dpsproject.Class.SubString;

import java.util.ArrayList;

/**
 * Created by david on 2016-11-16.
 */
public class Profile {

    private ArrayList<SubString> Subs;
    private ArrayList<SubString> Bookmarks;
    private ArrayList<SubString> Viewed;

    public Profile(){
        Subs= new ArrayList<SubString>();
        Bookmarks= new ArrayList<SubString>();

    }

    public Profile(ArrayList<SubString> posts, ArrayList<SubString> bookmarks){
        Subs=posts;
        Bookmarks=bookmarks;
    }

    public ArrayList<SubString> getBookmarks() {
        return Bookmarks;
    }

    public void setBookmarks(ArrayList<SubString> bookmarks) {
        Bookmarks = bookmarks;
    }
    public ArrayList<SubString> getSubs() {
        return Subs;
    }


    public void setSubs(ArrayList<SubString> posts) {
        Subs = posts;
    }
}
