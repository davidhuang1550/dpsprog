package com.example.david.dpsproject.Class;

import java.util.ArrayList;

/**
 * Created by david on 2016-12-26.
 */

public class postlist {


    private String Name;
    private ArrayList<String> postlist;

    public postlist(){
        postlist= new ArrayList<>();
    }

    public postlist(String n, ArrayList<String> po){
        Name=n;
        postlist=po;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<String> getPostlist() {
        return postlist;
    }

    public void setPostlist(ArrayList<String> postlist) {
        this.postlist = postlist;
    }

}
