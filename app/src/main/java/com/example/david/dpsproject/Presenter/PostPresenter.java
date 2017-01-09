package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Model.PostModel;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.ProgressBarPresenter;

/**
 * Created by david on 2016-12-20.
 */

public class PostPresenter {
    private PostModel postModel;

    public PostPresenter(Activity activity , DataBaseConnectionsPresenter dataBaseConnectionsPresenter, View view, SwipeRefreshLayout refresh, Users u){
        postModel = new PostModel(activity, dataBaseConnectionsPresenter,view,refresh,u);
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
    public void enableDisplayBySearch(){
        postModel.enableDisplayBySearch();
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
