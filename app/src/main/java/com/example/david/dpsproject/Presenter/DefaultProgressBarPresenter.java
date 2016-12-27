package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.david.dpsproject.R;

/**
 * Created by david on 2016-12-26.
 */

public class DefaultProgressBarPresenter {
    private View mProgressBarFooter;
    private Activity mActivity;
    private ListView listView;
    public DefaultProgressBarPresenter(Activity activity, ListView l){
        mActivity=activity;
        mProgressBarFooter = ((LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.defaultprogressbarheader, null, false);

        listView=l;
    }

    public void hidemProgressBarFooter(){
        listView.removeFooterView(mProgressBarFooter);
    }
    public void showmProgressBarFooter(){
        listView.addFooterView(mProgressBarFooter);
    }



}
