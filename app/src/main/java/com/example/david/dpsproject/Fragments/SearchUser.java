package com.example.david.dpsproject.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david.dpsproject.Presenter.SearchUserPresenter;
import com.example.david.dpsproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by david on 2016-12-25.
 */

public class SearchUser extends Fragment {

    private Activity mActivity;
    private View myView;
    private SearchUserPresenter searchUserPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.searchuserlayout,container,false);

        Bundle bundle = getArguments();
        if(bundle!=null){
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
            searchUserPresenter = new SearchUserPresenter(dbReference,myView,bundle.getString("username"),mActivity);
            searchUserPresenter.getUser();
        }


        return myView;
    }
}
