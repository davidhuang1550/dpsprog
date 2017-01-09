package com.example.david.dpsproject.Fragments.Search;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.SearchPostPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;

/**
 * Created by david on 2016-12-26.
 */

public class SearchPostFragment extends Fragment{
    private Activity mActivity;
    private View myView;
    private SwipeRefreshLayout refreshLayout;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    private SearchPostPresenter searchPostPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.searchpage,container,false);
        ((navigation)mActivity).showFab();
        dataBaseConnectionsPresenter = ((navigation)mActivity).getDataBaseConnectionsPresenter();
        Bundle bundle =getArguments();
        searchPostPresenter= new SearchPostPresenter(dataBaseConnectionsPresenter, myView, bundle.getString("SearchKey"), mActivity);
        searchPostPresenter.getFirstView();


        return myView;
    }
    public void onDestroy() {
        ViewGroup container = (ViewGroup)mActivity.findViewById(R.id.content_frame);
        container.removeAllViews();
        super.onDestroy();
    }

}
