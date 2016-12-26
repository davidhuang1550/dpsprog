package com.example.david.dpsproject.Class;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 2016-10-23.
 */
public class Users {

    private String userName;
    private String password;

    private String Picture;
    private String JoinDate;

    private int NumOfPosts;

    private Map<String,ArrayList<Post>> Posts;
    private ArrayList<String>Bookmarks;
    private ArrayList<String> Viewed;

    private ArrayList<String> Subcategory;
    //private Profile profile;

  //  private ArrayList<Posts> posts;


    public  Users(){
     //   profile = new Profile();
   //     Posts=new Map<String,ArrayList<Post>>();
        Bookmarks=new ArrayList<String>();
        Viewed=new ArrayList<String>();
    }
    public Users(String u, String p,Map<String,ArrayList<Post>> s,ArrayList<String> b,
                 ArrayList<String> v, String pic,ArrayList<String> subc, String join,int numPost) {
        userName = u;
        password = p;
        Posts=s;
        Bookmarks=b;
        Viewed=v;
        Picture=pic;
        Subcategory=subc;
        JoinDate=join;
        NumOfPosts=numPost;
      //  profile=pro;
    }

    public Users Users(Users users){

        Users u = new Users();
        //u.setProfile(users.getProfile());
        u.setPassword(users.getPassword());
        u.setUserName(users.getUserName());

        return u;
    }
    public int getNumOfPosts() {
        return NumOfPosts;
    }

    public void setNumOfPosts(int numOfPosts) {
        NumOfPosts = numOfPosts;
    }
    public String getJoinDate() {
        return JoinDate;
    }

    public void setJoinDate(String joinDate) {
        JoinDate = joinDate;
    }

    public ArrayList<String> getSubcategory() {
        return Subcategory;
    }

    public void setSubcategory(ArrayList<String> subcategory) {
        Subcategory = subcategory;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }
    public Map<String,ArrayList<Post>> getPosts() {
        return Posts;
    }

    public void setPosts(Map<String,ArrayList<Post>> posts) {
        Posts = posts;
    }

    public ArrayList<String> getBookmarks() {
        return Bookmarks;
    }

    public void setBookmarks(ArrayList<String> bookmarks) {
        Bookmarks = bookmarks;
    }

    public ArrayList<String> getViewed() {
        return Viewed;
    }

    public void setViewed(ArrayList<String> viewed) {
        Viewed = viewed;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addSub(String s){
        Subcategory.add(s);
    }
    public void deleteSub(String s){
        for(String temp:Subcategory){
            if(temp.equals(s)){
                Subcategory.remove(s);
                return;
            }
        }
    }
    public Bitmap getDecodedPicture(){
        byte[] decodedString = Base64.decode(getPicture(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
