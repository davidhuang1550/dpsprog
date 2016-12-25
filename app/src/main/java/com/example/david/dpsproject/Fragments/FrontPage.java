package com.example.david.dpsproject.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.Presenter.PostPresenter;
import com.example.david.dpsproject.Presenter.ProgressBarPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by david on 2016-10-25.
 * look into firebase auth refresh token because app will not work if the app is open for more than an hour
 */
public class FrontPage extends Fragment implements FragmentManager.OnBackStackChangedListener {
    View myView;

    private static ProgressBarPresenter progressBarPresenter;
    FirebaseAuth authentication;
    DatabaseReference dbReference;
    FloatingActionButton fab;
    NavigationView navigationView;
    FirebaseUser firebaseUser;
    SwipeRefreshLayout refreshLayout;
    Activity mActivity;
    ListView listView;
    Users user;
    int visibleItemCount;
    int totalItemCount;
    int firstVisibleItem;
    private PostPresenter postPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager= getFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        mActivity=getActivity();
    }
    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if(firebaseUser!=null) {

                nav_Menu.findItem(R.id.login).setVisible(false);
                nav_Menu.findItem(R.id.profile).setVisible(true);
                nav_Menu.findItem(R.id.signout).setVisible(true);

            ((navigation) mActivity).ShowProgressDialog();
              //  setPostView();
            postPresenter.setUserPost();
        }
        else if(bundle!=null){
            if (bundle.get("user").equals("true")) {
                postPresenter.setUserPost();
            } else if (bundle.get("user").equals("false")) {
                nav_Menu.findItem(R.id.login).setVisible(true);
                nav_Menu.findItem(R.id.profile).setVisible(false);
                nav_Menu.findItem(R.id.signout).setVisible(false);
              //  setDefaultPostView();
                postPresenter.setDefaultPost();
            }
        }

        else{
            //setDefaultPostView();
            postPresenter.setDefaultPost();
            nav_Menu.findItem(R.id.login).setVisible(true);
            nav_Menu.findItem(R.id.profile).setVisible(false);
            nav_Menu.findItem(R.id.signout).setVisible(false);
        }
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
        mActivity.setTitle("Front Page");
        myView = inflater.inflate(R.layout.front_page,container,false);
        ((navigation)mActivity).hideAllSubscribe();
        refreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swiperefresh);
        ((navigation)mActivity).setPostView(listView);

        authentication = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference(); // access to database
        firebaseUser = authentication.getCurrentUser();

        listView = (ListView)myView.findViewById(R.id.postview);
        postPresenter = new PostPresenter(mActivity , dbReference, myView, refreshLayout, user, firebaseUser);
        progressBarPresenter= new ProgressBarPresenter(mActivity, listView);
        postPresenter.setProgressBarPresenter(progressBarPresenter);



        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (listView != null){
                    final Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.splashfadeout);
                    listView.startAnimation(animation);

                    if(listView.getFooterViewsCount()==0){
                        MyPostAdapter tempAdapter= (MyPostAdapter)listView.getAdapter();
                        if(tempAdapter!=null)tempAdapter.clearData();
                        tempAdapter.notifyDataSetChanged();
                    }else{
                        progressBarPresenter.hidemProgressBarFooter();
                        progressBarPresenter.hideErrorBar();
                        HeaderViewListAdapter hlva = (HeaderViewListAdapter)listView.getAdapter();
                        MyPostAdapter postAdapter = (MyPostAdapter) hlva.getWrappedAdapter();
                        postAdapter.clearData();
                        postAdapter.notifyDataSetChanged();
                    }


                    refreshLayout.setRefreshing(true);
                    if(user!=null)  postPresenter.setUserPost();
                    else postPresenter.setDefaultPost();
                }
                else{
                    refreshLayout.setRefreshing(true);
                    if(user!=null)  postPresenter.setUserPost();
                    else postPresenter.setDefaultPost();
                }


            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(progressBarPresenter.getPin()==false) {
                        if(listView.getFooterViewsCount()==1){
                            progressBarPresenter.hideErrorBar();
                        }
                        postPresenter.addMoreItems();
                    }
                }
            }

            public void onScroll(AbsListView view, int firstVisible,
                                 int visibleItem, int totalItem) {
                visibleItemCount=visibleItem;
                totalItemCount=totalItem;
                firstVisibleItem=firstVisible;

            }
        });

        fab = (FloatingActionButton) mActivity.findViewById(R.id.compose);
        FloatingActionButton fab_image = (FloatingActionButton) mActivity.findViewById(R.id.compse_images);
        FloatingActionButton fab_desc = (FloatingActionButton) mActivity.findViewById(R.id.compse_desc);
        if(fab_image!=null)fab_image.hide();
        if(fab_desc!=null)fab_desc.hide();
        if(fab!=null)fab.show();


        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Menu nav_Menu = navigationView.getMenu();
        if(nav_Menu!=null)nav_Menu.findItem(R.id.search).setVisible(true);
        if(fab!=null)fab.show(); // when in front page you must show compose option
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
