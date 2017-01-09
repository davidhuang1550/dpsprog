package com.example.david.dpsproject.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DefaultProgressBarPresenter;
import com.example.david.dpsproject.Presenter.PostPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.ProgressBarPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;

/**
 * Created by david on 2016-10-25.
 * look into firebase auth refresh token because app will not work if the app is open for more than an hour
 */
public class FrontPage extends Fragment implements FragmentManager.OnBackStackChangedListener {
    View myView;

    private static ProgressBarPresenter progressBarPresenter;
    NavigationView navigationView;
    SwipeRefreshLayout refreshLayout;
    Activity mActivity;
    ListView listView;
    Users user;
    DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    private PostPresenter postPresenter;
    private DefaultProgressBarPresenter defaultProgressBarPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager= getFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        mActivity=getActivity();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        ViewGroup container = (ViewGroup)mActivity.findViewById(R.id.content_frame);
        container.removeAllViews();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBaseConnectionsPresenter = ((navigation)mActivity).getDataBaseConnectionsPresenter();
        mActivity.setTitle("Front Page");
        myView = inflater.inflate(R.layout.front_page,container,false);
        ((navigation)mActivity).hideAllSubscribe();
        refreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swiperefresh);
        ((navigation)mActivity).setPostView(listView);

        listView = (ListView)myView.findViewById(R.id.postview);
        postPresenter = new PostPresenter(mActivity , dataBaseConnectionsPresenter, myView, refreshLayout, user);
        progressBarPresenter= new ProgressBarPresenter(mActivity, listView);
        postPresenter.setProgressBarPresenter(progressBarPresenter);

        ((navigation)mActivity).showFab();
        init();


        return myView;
    }
    public void init(){
        Bundle bundle = getArguments();
        navigationView = (NavigationView)myView.findViewById(R.id.nav_view);
        if(((navigation)mActivity).getworkingUser()!=null) {
            ((navigation)mActivity).setLoginFalse();
            postPresenter.setUserPost();
        }
        else if(bundle!=null){
            if (bundle.get("user").equals("true")) {
                ((navigation)mActivity).setLoginFalse();
                postPresenter.setUserPost();
            } else if (bundle.get("user").equals("false")) {
                ((navigation)mActivity).setLogintrue();

                postPresenter.setDefaultPost();
            }
        }

        else{
            postPresenter.setDefaultPost();
            ((navigation)mActivity).setLogintrue();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
      /*  navigationView = (NavigationView) myView.findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        if(nav_Menu!=null)nav_Menu.findItem(R.id.search).setVisible(true);*/
        ((navigation)mActivity).showFab(); // when in front page you must show compose option
    }

    @Override
    public void onBackStackChanged() {
        final DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(mActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        if (((navigation) mActivity).getFragmentManager().getBackStackEntryCount() > 0) {
            ((navigation) mActivity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((navigation) mActivity).onBackPressed();
                }
            });
        } else {
            ((navigation) mActivity).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.syncState();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    drawer.openDrawer(GravityCompat.START);
                }
            });
        }
    }
}
