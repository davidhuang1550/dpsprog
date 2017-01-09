package com.example.david.dpsproject.Fragments.Search;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.SearchUserPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;

/**
 * Created by david on 2016-12-25.
 */

public class SearchUserFragment extends Fragment {

    private Activity mActivity;
    private View myView;
    private SearchUserPresenter searchUserPresenter;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.searchuserlayout,container,false);
        ((navigation)mActivity).hideFab();
        Bundle bundle = getArguments();
        if(bundle!=null){
            dataBaseConnectionsPresenter = ((navigation)mActivity).getDataBaseConnectionsPresenter();
            searchUserPresenter = new SearchUserPresenter(dataBaseConnectionsPresenter,myView,bundle.getString("username"),mActivity);
            searchUserPresenter.getUser();
        }


        return myView;
    }
}
