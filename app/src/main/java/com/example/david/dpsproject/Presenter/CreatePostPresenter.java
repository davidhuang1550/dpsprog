package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.app.Fragment;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Model.CreatePostModel;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;

/**
 * Created by david on 2017-01-03.
 */

public class CreatePostPresenter {
    private CreatePostModel createPostModel;

    public CreatePostPresenter(Post p, Activity activity, DataBaseConnectionsPresenter dataBaseConnectionsPresenter, String sub, Users u, Fragment fragment){
        createPostModel = new CreatePostModel(p,activity, dataBaseConnectionsPresenter,sub,u,fragment);
    }
    public void createPost(){
        try {
            createPostModel.createPost();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
