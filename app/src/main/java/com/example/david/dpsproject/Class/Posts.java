package com.example.david.dpsproject.Class;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by david on 2016-11-19.
 */
public class Posts {



    private String Name;
    private ArrayList<String> postlist;

    public Posts(){
        postlist= new ArrayList<>();
    }

    public Posts(String n, ArrayList<String> po){
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
