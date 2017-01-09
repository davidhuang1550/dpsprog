package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.view.View;

import com.example.david.dpsproject.Model.SearchUserModel;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;

import java.util.concurrent.ExecutionException;

/**
 * Created by david on 2016-12-25.
 */

public class SearchUserPresenter {
    private SearchUserModel searchUserModel;

    public SearchUserPresenter(DataBaseConnectionsPresenter dataBaseConnectionsPresenter, View myView, String name, Activity activity){
        searchUserModel= new SearchUserModel(dataBaseConnectionsPresenter,myView,name,activity);
    }
    public void getUser(){
        try {
            searchUserModel.getUser();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
