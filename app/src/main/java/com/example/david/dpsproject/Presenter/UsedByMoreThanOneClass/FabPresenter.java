package com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;

import com.example.david.dpsproject.Model.FabModel;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;

/**
 * Created by david on 2017-01-07.
 */

public class FabPresenter {
    private FabModel fabModel;
    public FabPresenter(FloatingActionButton f1, FloatingActionButton f2, FloatingActionButton f3, DataBaseConnectionsPresenter dataBaseConnectionsPresenter, Activity activity){
        fabModel= new FabModel(f1,f2,f3,dataBaseConnectionsPresenter,activity);
    }
    public void showFab(){
        fabModel.showFab();
    }
    public void hideFab(){
        fabModel.hideFab();
    }
}
