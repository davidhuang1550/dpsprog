package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.view.View;

import com.example.david.dpsproject.Model.SearchUserModel;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by david on 2016-12-25.
 */

public class SearchUserPresenter {
    private SearchUserModel searchUserModel;

    public SearchUserPresenter(DatabaseReference db, View myView, String name, Activity activity){
        searchUserModel= new SearchUserModel(db,myView,name,activity);
    }
    public void getUser(){
        searchUserModel.getUser();
    }
}
