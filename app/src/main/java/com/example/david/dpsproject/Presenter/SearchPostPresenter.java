package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.Model.SearchPostModel;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by david on 2016-12-26.
 */

public class SearchPostPresenter {
    private SearchPostModel searchPostModel;

    public SearchPostPresenter(DatabaseReference db, View view, String s, Activity activity){
        searchPostModel = new SearchPostModel(db,view,s,activity);
    }
    public void getFirstView(){
        searchPostModel.getFirstView();
    }
}
