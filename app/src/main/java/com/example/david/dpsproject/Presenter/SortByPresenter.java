package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.dpsproject.Model.SortByModel;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;

/**
 * Created by david on 2016-12-28.
 */

public class SortByPresenter
{
    private SortByModel sortByModel;
    private Activity mActivity;
    public SortByPresenter(DataBaseConnectionsPresenter dataBaseConnectionsPresenter, String sub, Activity activity, ListView listView, View view){
        mActivity= activity;
        sortByModel = new SortByModel(dataBaseConnectionsPresenter,sub,mActivity,listView,view);
    }
    public void SortByYes(){
            sortByModel.SortByYes();
    }
    public void SortByNo(){
      sortByModel.SortByNo();
    }
    public void SortByResponse(){
        sortByModel.SortByResponse();
    }

    public void sort() {
        try {
            sortByModel.sort();
        }catch (IndexOutOfBoundsException e){
            Toast.makeText(mActivity,"Nothing was found", Toast.LENGTH_SHORT).show();
        }
    }
}
