package com.example.david.dpsproject.Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by david on 2016-10-28.
 */
public class Post implements Serializable {

    private int No;
    private int Yes;
    private String PosterId;
    private String Title;
    private String key;
    private String Description;
    private Long Timestamp;
    private String SubN;
    private int totalPost;
    private String Image;
    private ArrayList<Comment> comments;
    private HashMap<String,Integer> piechart;
    public Post(){

    }

    public Post(String key, int no, int yes, String posterId, String title, ArrayList<Comment> comments, String description,String image,
                Long T,String sn,int total,HashMap<String,Integer> pie){

        this.key = key;
        No = no;
        Timestamp=T;
        Yes = yes;
        PosterId = posterId;
        Title = title;
        this.comments = comments;
        Description = description;
        Image=image;
        SubN=sn;
        totalPost=total;
        piechart=pie;
    }
    public Post(String posterId,String title,String description,Long T,String sn){
        totalPost=0;
        PosterId=posterId;
        Title=title;
        Timestamp=T;
        Description=description;
        SubN=sn;
    }
    public Post(String posterId,String title,Long T,String sn){
        totalPost=0;
        PosterId=posterId;
        Title=title;
        Timestamp=T;
        SubN=sn;
    }
    public HashMap<String, Integer> getPiechart() {
        return piechart;
    }

    public void setYesNo(){
        Yes=0;No=0;
    }
    public void setPiechart(HashMap<String, Integer> piechart) {
        this.piechart = piechart;
    }
    public int getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(int totalPost) {
        this.totalPost = totalPost;
    }
    public void incTotalPost(){
        totalPost++;
    }
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
    public Long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Long timestamp) {
        Timestamp = timestamp;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public int getYes() {
        return Yes;
    }

    public void setYes(int yes) {
        Yes = yes;
    }

    public String getPosterId() {
        return PosterId;
    }

    public void setPosterId(String posterId) {
        PosterId = posterId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void IncNo(){
        No++;
    }
    public void IncYes(){
        Yes++;
    }
    public String getSubN() {
        return SubN;
    }

    public void setSubN(String subN) {
        SubN = subN;
    }

}
