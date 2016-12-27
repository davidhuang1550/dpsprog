package com.example.david.dpsproject.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.PostPresenter;
import com.example.david.dpsproject.Presenter.SearchPostPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by david on 2016-12-26.
 */

public class SearchPostFragment extends Fragment{
    private Activity mActivity;
    private View myView;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference dbReference;
    private SearchPostPresenter searchPostPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.searchpage,container,false);
        dbReference = FirebaseDatabase.getInstance().getReference(); // access to database
        Bundle bundle =getArguments();
        searchPostPresenter= new SearchPostPresenter(dbReference, myView, bundle.getString("SearchKey"), mActivity);
        searchPostPresenter.getFirstView();


        return myView;
    }
    public void onDestroy() {
        ViewGroup container = (ViewGroup)mActivity.findViewById(R.id.content_frame);
        container.removeAllViews();
        super.onDestroy();
    }

}
