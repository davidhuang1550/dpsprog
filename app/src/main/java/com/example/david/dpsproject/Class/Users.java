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
    private String FcmToken;
    private HashMap<String, ArrayList<String>> Posts;
    private HashMap<String, ArrayList<String>>Bookmarks;
    private HashMap<String, ArrayList<String>> Viewed;

    private ArrayList<String> Subcategory;
    //private Profile profile;

  //  private ArrayList<Posts> posts;


    public  Users(){
        Posts=new HashMap<String, ArrayList<String>>();
        Bookmarks=new HashMap<String, ArrayList<String>>();
        Viewed=new HashMap<String, ArrayList<String>>();
    }
    public Users(String u, String p,HashMap<String, ArrayList<String>> s,HashMap<String, ArrayList<String>> b,
                 HashMap<String, ArrayList<String>> v, String pic,ArrayList<String> subc, String join,int numPost, String fcm) {
        userName = u;
        password = p;
        Posts=s;
        Bookmarks=b;
        Viewed=v;
        Picture=pic;
        Subcategory=subc;
        JoinDate=join;
        NumOfPosts=numPost;
        FcmToken=fcm;
      //  profile=pro;
    }

    public Users Users(Users users){

        Users u = new Users();
        u.setPassword(users.getPassword());
        u.setUserName(users.getUserName());

        return u;
    }
    public String getFcmToken() {
        return FcmToken;
    }

    public void setFcmToken(String fcmToken) {
        FcmToken = fcmToken;
    }

    public void IncNumOfPosts(){
        NumOfPosts++;
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
    public HashMap<String, ArrayList<String>> getPosts() {
        return Posts;
    }

    public void setPosts(HashMap<String, ArrayList<String>> posts) {
        Posts = posts;
    }

    public HashMap<String, ArrayList<String>> getBookmarks() {
        return Bookmarks;
    }

    public void setBookmarks(HashMap<String, ArrayList<String>> bookmarks) {
        Bookmarks = bookmarks;
    }

    public HashMap<String, ArrayList<String>> getViewed() {
        return Viewed;
    }

    public void setViewed(HashMap<String, ArrayList<String>> viewed) {
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
    public void addToHashMapPost(String sub, String post){
        ArrayList<String> p = Posts.get(sub);
        if(p!=null){
            p.add(post);
        }
        else{
            ArrayList<String> tempPost = new ArrayList<>();
            tempPost.add(post);
            Posts.put(sub,tempPost);
        }
    }
    public boolean findArrayListPost(String sub,String post){
        ArrayList<String> posts = Viewed.get(sub);
        boolean returnval=true;
        if(posts!=null){
            if(posts.contains(post)){
                returnval=false;
            }
        }
        return returnval;
    }
    public ArrayList<String> AddToViewed(String sub,String key){
        ArrayList<String> templist=Viewed.get(sub);
        if(templist==null){
            templist= new ArrayList<>();
        }
         templist.add(key);
        return templist;
    }
    public ArrayList<String> AddToBookMarked(String sub,String key){
        ArrayList<String> templist=Bookmarks.get(sub);
        if(templist==null){
            templist= new ArrayList<>();
        }
        templist.add(key);
        return templist;
    }
    public ArrayList<String> removeFromBookMarked(String sub,String key){
        ArrayList<String> templist=Bookmarks.get(sub);
        if(templist!=null)templist.remove(key);
        return templist;
    }
    public boolean findBookMark(String sub, String key){
        ArrayList<String> templist=Bookmarks.get(sub);
        if(templist!=null){
            return (templist.contains(key))?true:false;
        }
        return false;
    }

}
