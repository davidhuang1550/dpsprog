package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Model.PostModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by david on 2016-12-20.
 */

public class PostPresenter {
    private PostModel postModel;

    public PostPresenter(Activity activity , DatabaseReference db, View view, SwipeRefreshLayout refresh, Users u, FirebaseUser fbu){
        postModel = new PostModel(activity,db,view,refresh,u,fbu);
    }
    public void setDefaultPost(){
        postModel.setDefaultPostView();
    }
    public void setUserPost()
    {
        try {
            postModel.setPostView();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }
    public void setProgressBarPresenter(ProgressBarPresenter progressBarPresenter){
        postModel.setProgressBarPresenter(progressBarPresenter);
    }

    public void setSearchPost(){
        try {
            postModel.setSearchView();
        }catch (Exception e){
            Log.e("Tag","Error");
        }
    }
    public void addMoreItems(){postModel.addmoreItems();}
    public void setCategory(String cat){
        postModel.setCategory(cat);
    }


}
