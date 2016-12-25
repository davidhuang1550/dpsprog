package com.example.david.dpsproject.Presenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.david.dpsproject.R;

/**
 * Created by david on 2016-12-19.
 */

public class ProgressBarPresenter {

    private View mProgressBarFooter;
    private boolean pin;
    private Activity mActivity;
    private View ErrorBar;
    private ListView listView;
    public ProgressBarPresenter(Activity activity, ListView l){
        pin=false;
        mActivity=activity;
        mProgressBarFooter = ((LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progress_bar_footer, null, false);

        ErrorBar= ((LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loadingerror, null, false);
        listView=l;
    }


    public void hidemProgressBarFooter(){
        pin=false;
        listView.removeFooterView(mProgressBarFooter);
    }
    public void setPostView(ListView l){listView=l;}
    public void showmProgressBarFooter(){
        pin=true;
        listView.addFooterView(mProgressBarFooter);
    }
    public void showErrorBar(){
        listView.addFooterView(ErrorBar);
    }
    public boolean getPin(){return pin;}
    public void hideErrorBar(){listView.removeFooterView(ErrorBar);}

}
