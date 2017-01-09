package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.example.david.dpsproject.Model.SearchPostModel;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;

/**
 * Created by david on 2016-12-26.
 */

public class SearchPostPresenter {
    private SearchPostModel searchPostModel;
    private Activity mActivity;
    public SearchPostPresenter(DataBaseConnectionsPresenter dataBaseConnectionsPresenter, View view, String s, Activity activity){
        searchPostModel = new SearchPostModel(dataBaseConnectionsPresenter,view,s,activity);
        mActivity=activity;
    }
    public void getFirstView(){
        try {
            searchPostModel.getFirstView();
        }catch (ArrayIndexOutOfBoundsException e){
            Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
        }
    }
}
